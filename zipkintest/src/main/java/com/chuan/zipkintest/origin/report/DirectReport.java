package com.chuan.zipkintest.origin.report;

import zipkin2.Endpoint;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

/**
 * @author chuanjiang
 */
public class DirectReport {
    private static final AsyncReporter<Span> reporter;

    static {
        String zipkinAddress = "http://node1:9411/api/v2/spans";

        OkHttpSender sender = OkHttpSender.newBuilder()
                .endpoint(zipkinAddress)
                .build();

        reporter = AsyncReporter.builder(sender).build();
    }

    // 1 ->(s1) 2 ->(s2) 4
    //   ->(s3) 3
    public static void main(String[] args) {

        String traceId = "6";

        long start1 = System.currentTimeMillis() * 1000;
        long end1 = start1 + 800;
        Span span1 = Span.newBuilder()
                .name("span1")
                .traceId(traceId)
                .id("1")
                .parentId(null)
                .localEndpoint(Endpoint.newBuilder().serviceName("service1").build())
                .remoteEndpoint(Endpoint.newBuilder().serviceName("service2").build())
                .timestamp(start1)
                .duration(end1 - start1)
                .addAnnotation(start1, "cs")
                .addAnnotation(start1 + 100, "sr")
                .addAnnotation(start1 + 700, "ss")
                .addAnnotation(start1 + 800, "cr")
                .putTag("fromTo", "service1->service2")
                .build();
        reporter.report(span1);

        long start2 = start1 + 200;
        long end2 = start1 + 600;
        Span span2 = Span.newBuilder()
                .name("span2")
                .traceId(traceId)
                .id("2")
                .parentId("1")
                .localEndpoint(Endpoint.newBuilder().serviceName("service2").build())
                .remoteEndpoint(Endpoint.newBuilder().serviceName("service4").build())
                .timestamp(start2)
                .duration(end2 - start2)
                .addAnnotation(start2, "cs")
                .addAnnotation(start2 + 100, "sr")
                .addAnnotation(start2 + 200, "ss")
                .addAnnotation(start2 + 300, "cr")
                .putTag("fromTo", "service2->service4")
                .build();
        reporter.report(span2);

        long start3 = start1 + 800;
        long end3 = start1 + 1000;
        Span span3 = Span.newBuilder()
                .name("span3")
                .traceId(traceId)
                .id("3")
                .parentId(null)
                .localEndpoint(Endpoint.newBuilder().serviceName("service1").build())
                .remoteEndpoint(Endpoint.newBuilder().serviceName("service3").build())
                .timestamp(start3)
                .duration(end3 - start3)
                .addAnnotation(start3, "cs")
                .addAnnotation(start3 + 50, "sr")
                .addAnnotation(start3 + 100, "ss")
                .addAnnotation(start3 + 150, "cr")
                .putTag("fromTo", "service1->service3")
                .build();
        reporter.report(span3);

        reporter.flush();
        reporter.close();
    }


}
