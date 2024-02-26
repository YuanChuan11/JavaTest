package com.chuan.zipkintest.report.origin;

import zipkin2.Endpoint;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;

@Deprecated
public class ReportService {
    private AsyncReporter<Span> reporter;

    private String serviceName;

    Endpoint localEndpoint;

    public ReportService(AsyncReporter<Span> reporter, String serviceName) {
        this.reporter = reporter;
        this.serviceName = serviceName;
        this.localEndpoint = Endpoint.newBuilder().serviceName(serviceName).build();
    }

    public void reportClientStart(String traceId, String parentId, String spanId, String spanName, long waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long ts = System.currentTimeMillis() * 1000;
        Span span = Span.newBuilder()
                .name(spanName)
                .traceId(traceId)
                .id(spanId)
                .parentId(parentId)
                .localEndpoint(localEndpoint)
                .timestamp(ts)
                .addAnnotation(ts, "cs")
                .putTag("serviceName", serviceName)
                .build();
        reporter.report(span);
    }

    public void reportClientReceive(String traceId, String parentId, String spanId, String spanName, long waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long ts = System.currentTimeMillis() * 1000;
        Span span = Span.newBuilder()
                .name(spanName)
                .traceId(traceId)
                .id(spanId)
                .parentId(parentId)
                .localEndpoint(localEndpoint)
                .timestamp(ts)
                .addAnnotation(ts, "cr")
                .putTag("serviceName", serviceName)
                .build();
        reporter.report(span);
    }

    //return spanId
    public void reportServiceStart(String traceId, String parentId, String spanId, String spanName, long waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long ts = System.currentTimeMillis() * 1000;
        Span span = Span.newBuilder()
                .name(spanName)
                .traceId(traceId)
                .id(spanId)
                .parentId(parentId)
                .localEndpoint(localEndpoint)
                .timestamp(ts)
                .addAnnotation(ts, "cs")
                .putTag("serviceName", serviceName)
                .build();
        reporter.report(span);
    }

    public void reportServiceReceive(String traceId, String parentId, String spanId, String spanName, long waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long ts = System.currentTimeMillis() * 1000;
        Span span = Span.newBuilder()
                .name(spanName)
                .traceId(traceId)
                .id(spanId)
                .parentId(parentId)
                .localEndpoint(localEndpoint)
                .timestamp(ts)
                .addAnnotation(ts, "cr")
                .putTag("serviceName", serviceName)
                .build();
        reporter.report(span);
    }
}
