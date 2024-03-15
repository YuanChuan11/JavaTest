package com.chuan.jaegertest.opentracing;

import com.chuan.jaegertest.config.JaegerConfig;
import io.jaegertracing.Configuration;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * @author chuanjiang
 */
public class BaggageDemo {

    public static void main(String[] args) {
        baggageTest();
    }


    /*  baggage在span中单向传递, 且相比brave, jaeger会上报随行数据 */
    public static void baggageTest() {
        Configuration configuration = JaegerConfig.getConfiguration("jaeger-baggage");

        Tracer tracer = configuration.getTracer();
        Span parentSpan = tracer.buildSpan("parent-span").start();
        // 父span设置baggage, 会传递给子span
        parentSpan.setBaggageItem("parent.baggage", "parent.value");

        Span childSpan = tracer.buildSpan("child-span").asChildOf(parentSpan).start();
        // 子span设置baggage, 不会影响父span
        childSpan.setBaggageItem("child.baggage", "child.value");
        System.out.println(childSpan.getBaggageItem("parent.baggage"));
        // 子span设置父span设置过的baggage, 会覆盖但父span不可见此修改
        childSpan.setBaggageItem("parent.baggage", "parent.child.value");
        System.out.println(childSpan.getBaggageItem("parent.baggage"));
        childSpan.finish();

        System.out.println(parentSpan.getBaggageItem("child.baggage"));
        System.out.println(parentSpan.getBaggageItem("parent.baggage"));

        parentSpan.finish();
        tracer.close();
    }
}
