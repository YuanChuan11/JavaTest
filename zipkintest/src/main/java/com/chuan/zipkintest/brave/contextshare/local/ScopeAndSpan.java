package com.chuan.zipkintest.brave.contextshare.local;

import brave.ScopedSpan;
import brave.Span;
import brave.Tracer;
import brave.Tracing;

// 优雅实现 避免暴露 TraceContext和 CurrentTraceContext
public class ScopeAndSpan {

    public static void testScopedSpan(){
        Tracing tracing = Tracing.newBuilder().localServiceName("testScopedSpan-service")
                .build();
        Tracer tracer = tracing.tracer();
        ScopedSpan parentSpan = tracer.startScopedSpan("parent-span");
        System.out.println(tracer.currentSpan());

        Span childSpan = tracer.nextSpan().name("child-span").start();
        System.out.println(childSpan.context().parentIdString());

        childSpan.finish();
        parentSpan.finish();
        tracing.close();
    }

    public static void testSpanInScope(){
        Tracing tracing = Tracing.newBuilder().localServiceName("testSpanInScope-service")
                .build();
        Tracer tracer = tracing.tracer();
        Span parentSpan = tracer.nextSpan().name("parent-span").start();
        System.out.println(tracer.currentSpan());
        Tracer.SpanInScope scope = tracer.withSpanInScope(parentSpan);
        System.out.println(tracer.currentSpan());
        Span childSpan = tracer.nextSpan().name("child-span").start();
        System.out.println(childSpan.context().parentIdString());

        childSpan.finish();
        parentSpan.finish();
        scope.close();
        tracing.close();
    }
    public static void main(String[] args) {
        //testScopedSpan();
        testSpanInScope();
    }
}
