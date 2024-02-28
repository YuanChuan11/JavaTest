package com.chuan.zipkintest.brave.report;


import brave.Span;
import brave.Tracer;
import brave.Tracing;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

public class BraveReport {
    private static final AsyncReporter<zipkin2.Span> reporter;
    private static final OkHttpSender sender;

    static {
        String zipkinAddress = "http://node1:9411/api/v2/spans";

        sender = OkHttpSender.newBuilder()
                .endpoint(zipkinAddress)
                .build();

        reporter = AsyncReporter.create(sender);
    }

    public static void main(String[] args) throws InterruptedException {
        Tracing tracing = Tracing.newBuilder()
                .localServiceName("brave-service")
                .spanReporter(reporter)
                .build();

        Tracer tracer = tracing.tracer();
        Span span = tracer.newTrace().name("parent-span").start();
        Thread.sleep(100);
        Span childSpan = tracer.newChild(span.context()).name("child-span").start();
        Thread.sleep(50);
        childSpan.finish();
        Thread.sleep(100);
        span.error(new RuntimeException("test error123"));
        span.finish();

        tracing.close();
        reporter.close();
        sender.close();

    }
}
