package com.chuan.zipkintest.brave.contextshare.local;

import brave.ScopedSpan;
import brave.Span;
import brave.Tracer;
import brave.Tracing;

/**
 * Span自动处理跨度数据的本地传播的优雅实现 避免暴露 TraceContext和 CurrentTraceContext
 * 处理ScopeSpan时推荐使用startScopedSpan()创建
 *
 * @author chuanjiang
 */
public class ScopeAndSpan {

    //ScopedSpan 相比于Span多了一个scope属性,包含scope的上下文传播能力.
    //1.创建的时候会直接替换当前上下文 2.在finish()时比Span多了一步,会恢复之前保存的span上下文
    //即ScopedSpan可以自动处理跨度数据的本地传播, 而Span需要在代码中显示处理
    //ScopedSpan 和 Span 都继承了SpanCustomizer,
    //如果只修改name, tags, annotations, 推荐使用SpanCustomizer, 避免暴露其他细节
    public static void testScopedSpan() {
        Tracing tracing = Tracing.newBuilder().localServiceName("testScopedSpan-service")
                .build();
        Tracer tracer = tracing.tracer();
        ScopedSpan parentSpan = tracer.startScopedSpan("parent-span");
        System.out.println(tracer.currentSpan());

        Span childSpan = tracer.nextSpan().name("child-span").start();
        System.out.println(childSpan.context().parentIdString());
        System.out.println(tracer.currentSpan());

        childSpan.finish();
        parentSpan.finish();
        tracing.close();
    }

    // 虽然ScopeSpan能屏蔽TraceContext和CurrentTraceContext的细节,
    // 但有些情况必须使用Span时,可以使用Tracer.SpanInScope,也能屏蔽TraceContext和CurrentTraceContext的细节
    // SpanInScope是Tracer的内部类, 作用和Scope一样, 也要手动调用close方法 .但是避免暴露了TraceContext.
    public static void testSpanInScope() {
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
        testScopedSpan();
        System.out.println("==================================");
        testSpanInScope();
    }
}
