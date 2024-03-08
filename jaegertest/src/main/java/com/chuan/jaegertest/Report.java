package com.chuan.jaegertest;

import io.jaegertracing.Configuration;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * @author chuanjiang
 */
public class Report {

    public static void main(String[] args) {
        //demoTest();
        baggageTest();
    }

    public static void demoTest() {
        Configuration configuration = JaegerConfig.GetConfiguration("jaeger-demo");

        Tracer tracer = configuration.getTracer();
        Span span = tracer.buildSpan("jaeger-span").start();

        span.finish();
        tracer.close();
    }

    public static void baggageTest() {
        Configuration configuration = JaegerConfig.GetConfiguration("jaeger-baggage");

        Tracer tracer = configuration.getTracer();
        Span parentSpan = tracer.buildSpan("parent-span").start();
        parentSpan.setBaggageItem("parent.baggage", "parent.value");

        Span childSpan = tracer.buildSpan("child-span").asChildOf(parentSpan).start();
        childSpan.setBaggageItem("child.baggage", "child.value");
        System.out.println(childSpan.getBaggageItem("parent.baggage"));
        childSpan.setBaggageItem("parent.baggage", "parent.child.value");
        System.out.println(childSpan.getBaggageItem("parent.baggage"));
        childSpan.finish();

        System.out.println(parentSpan.getBaggageItem("child.baggage"));
        System.out.println(parentSpan.getBaggageItem("parent.baggage"));

        parentSpan.finish();
        tracer.close();
    }

}