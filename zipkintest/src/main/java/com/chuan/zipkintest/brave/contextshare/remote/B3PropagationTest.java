package com.chuan.zipkintest.brave.contextshare.remote;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chuanjiang
 */
public class B3PropagationTest {

    public static void main(String[] args) {
        Tracing tracing = Tracing.newBuilder().localServiceName("B3PropagationTest-service")
                .build();
        Map<String, String> carrier = new HashMap<>();
        TraceContext.Injector<Map<String, String>> injector = tracing.propagation()
                .injector(Map::put);
        TraceContext.Extractor<Map<String, String>> extractor = tracing.propagation()
                .extractor(Map::get);

        Tracer tracer = tracing.tracer();
        Span parentSpan = tracer.nextSpan().name("parent-span").start();
        System.out.println(parentSpan);
        //inject
        injector.inject(parentSpan.context(), carrier);
        System.out.println(carrier);
        //extract
        Span childSpan = tracer.nextSpan(extractor.extract(carrier))
                .name("child-span").start();

        System.out.println(childSpan);
        System.out.println(childSpan.context().parentIdString());
        System.out.println(childSpan.context().spanIdString());

        childSpan.finish();
        parentSpan.finish();
        tracing.close();
    }
}
