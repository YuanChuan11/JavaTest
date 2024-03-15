package com.chuan.jaegertest.opentracing.context;

import com.chuan.jaegertest.config.JaegerConfig;
import io.jaegertracing.Configuration;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * 通过{@link io.opentracing.ScopeManager} 实现进程方法span上下文传递
 * activate(span)激活span并保存到当前线程的ThreadLocal中
 * activeSpan()获取当前线程的span
 * <p>
 *
 * 通过{@link io.opentracing.Scope} 来结束span上下文的传递
 * close()结束span上下文的传递 删除当前线程的ThreadLocal中的span
 *
 * @author chuanjiang
 */
public class ScopeDemo {

    static Configuration configuration = JaegerConfig.getConfiguration("scope-demo");
    static Tracer tracer = configuration.getTracer();

    public static void childSpan() {
        // 通过buildSpan创建的span默认为当前content中激活的span的子span
        Span span = tracer.buildSpan("child-span")
                .start();
        System.out.println(span);
        span.finish();
    }

    public static void noChildSpan() {
        // ignoreActiveSpan()忽略当在content中已激活的span
        Span span = tracer.buildSpan("no-child-span")
                .ignoreActiveSpan()
                .start();
        System.out.println(span);
        span.finish();
    }

    public static void main(String[] args) {
        Span span = tracer.buildSpan("parent-span").start();
        // 激活跨度，span会保存到当前线程的ThreadLocal中
        Scope scope = tracer.activateSpan(span);
        System.out.println(span);

        childSpan();
        noChildSpan();
        Thread t1 = new Thread(() -> {
            // 默认ScopeManager实现是ThreadLocal是不会继承给子线程的
            // 可以自定义ScopeManager用InheritableThreadLocal实现跨进程传递的context
            // 自定义ScopeManager需要在Tracer初始化时指定
            Span span1 = tracer.buildSpan("thread-span").start();
            System.out.println(span1);
            span1.finish();
        });
        t1.start();
        scope.close();
        span.finish();
        tracer.close();
    }
}
