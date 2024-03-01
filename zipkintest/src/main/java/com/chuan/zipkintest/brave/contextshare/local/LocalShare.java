package com.chuan.zipkintest.brave.contextshare.local;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;

/**
 * 用Scope显示处理跨度数据的本地传播
 * currentSpan()和nextSpan()更高级的创建span
 * 处理Span时推荐使用nextSpan()创建
 *
 * @author chuanjiang
 */
public class LocalShare {

    // maybeScope根据将当前上下文的span和传入的span进行比较，如果不相等,才替换当前上下文的span,并返回一个新的scope
    // scope.close()会恢复被替换的span
    public static void testMaybeScope() {
        Tracing tracing = Tracing.newBuilder().localServiceName("current-trace-context-service")
                .build();
        Tracer tracer = tracing.tracer();
        Span span = tracer.newTrace().name("current-trace-context-span").start();

        // 此时trace context为空， 和span的context不相等，将会设置为span的context
        CurrentTraceContext.Scope scope = tracing.currentTraceContext().maybeScope(span.context());
        System.out.println(tracing.currentTraceContext().get() == span.context());

        // trace的context从span的context回退为null
        scope.close();
        System.out.println(tracing.currentTraceContext().get());

        span.finish();
        tracing.close();
    }

    // currentSpan()会根据当前上下文返回一个新的lazySpan, 性能好
    //
    // nextSpan()会判断当前上下文的span是否为空,
    // 如果为空,则返回一个新的Span(等效于newTrace()).
    // 如果不为空,则返回一个以当前上下文为parent的span(等效于newChild()).
    // 故nextSpan()代替newTrace()和newChild()
    public static void testNextSpan() {
        Tracing tracing = Tracing.newBuilder()
                .localServiceName("current-trace-context-next-span-service")
                .build();
        Tracer tracer = tracing.tracer();
        Span parentSpan = tracer.newTrace().name("parent-span").start();
        System.out.println("init:  -->");
        System.out.println("parentSpan.context().spanIdString():" + parentSpan.context().spanIdString());
        System.out.println("tracer.currentSpan():" + tracer.currentSpan());

        CurrentTraceContext.Scope scope = tracing.currentTraceContext().maybeScope(parentSpan.context());
        System.out.println("after maybeScope:  -->");
        System.out.println("tracer.currentSpan():" + tracer.currentSpan());

        Span chileSpan = tracer.nextSpan().name("chile-span").start();
        System.out.println("after nextSpan:  -->");
        System.out.println("chileSpan.context().parentIdString():" + chileSpan.context().parentIdString());
        System.out.println("chileSpan.context().spanIdString():" + chileSpan.context().spanIdString());
        System.out.println("tracer.currentSpan():" + tracer.currentSpan());

        chileSpan.finish();
        scope.close();
        System.out.println("tracer.currentSpan():" + tracer.currentSpan());

        chileSpan.finish();
        tracing.close();
    }

    public static void main(String[] args) {
        testMaybeScope();
        System.out.println("==================================");
        testNextSpan();
    }

}
