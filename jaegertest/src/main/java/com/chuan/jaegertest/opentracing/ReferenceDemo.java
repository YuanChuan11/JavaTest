package com.chuan.jaegertest.opentracing;

import com.chuan.jaegertest.config.JaegerConfig;
import io.jaegertracing.Configuration;
import io.opentracing.References;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * @author chuanjiang
 */
public class ReferenceDemo {
    public static void main(String[] args) throws InterruptedException {
        Configuration configuration = JaegerConfig.getConfiguration("jaeger-demo");

        Tracer tracer = configuration.getTracer();
        Span span = tracer.buildSpan("parent-span").start();

        Thread.sleep(10);
        // opentracing中通过childOf表示有依赖关系的引用, 父span依赖子span的执行结果
        Span childSpan = tracer.buildSpan("child-span").addReference(References.CHILD_OF, span.context()).start();
        // 上述引用和asChildOf方法实现一样
        // Span childSpan = tracer.buildSpan("child-span").asChildOf(span).start();

        // FOLLOWS_FROM引用, 表示同级关系,没有依赖, 如消息队列之间.
        // Span childSpan = tracer.buildSpan("child-span").addReference(References.FOLLOWS_FROM, span.context()).start();

        Thread.sleep(10);
        childSpan.finish();
        span.finish();
        tracer.close();
    }
}

