package com.chuan.zipkintest.brave.contextshare.local;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;

// 原生实现
public class LocalShare {

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

    public static void testNextSpan() {
        Tracing tracing = Tracing.newBuilder()
                .localServiceName("current-trace-context-next-span-service")
                .build();
        Tracer tracer = tracing.tracer();
        Span parentSpan = tracer.newTrace().name("parent-span").start();
        System.out.println("init:  -->");
        System.out.println("parentSpan.context().spanIdString():"+parentSpan.context().spanIdString());
        System.out.println("tracer.currentSpan():"+tracer.currentSpan());

        CurrentTraceContext.Scope scope = tracing.currentTraceContext().maybeScope(parentSpan.context());
        System.out.println("after maybeScope:  -->");
        System.out.println("tracer.currentSpan():"+tracer.currentSpan());

        Span chileSpan = tracer.nextSpan().name("chile-span").start();
        System.out.println("after nextSpan:  -->");
        System.out.println("chileSpan.context().parentIdString():"+chileSpan.context().parentIdString());
        System.out.println("chileSpan.context().spanIdString():"+chileSpan.context().spanIdString());
        System.out.println("tracer.currentSpan():"+tracer.currentSpan());

        chileSpan.finish();
        scope.close();
        System.out.println("tracer.currentSpan():"+tracer.currentSpan());

        chileSpan.finish();
        tracing.close();
    }

    public static void main(String[] args) {
        //testMaybeScope();
        testNextSpan();
    }

}
