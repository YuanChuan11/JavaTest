package com.chuan.jaegertest.jsdk;

import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.spi.Reporter;
import io.jaegertracing.zipkin.ZipkinV2Reporter;
import io.opentracing.Span;
import io.opentracing.Tracer;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

/**
 * 需要引入单独依赖 jaeger-zipkin
 *
 * @author chuanjiang
 */
public class ZipkinV2ToJaeger {
    public static void main(String[] args) {
        /* v1版本的zipkin上报

        String v1URL = "http://localhost:9411/api/v1/spans";
        Sender sender = ZipkinSender.create(v1URL);
        Reporter reporter = new RemoteReporter.Builder().withSender(sender).build();

        */


        // 使用zipkin v2版本 上报到jaeger
        String v2URL = "http://node1:9411/api/v2/spans";
        Reporter reporter = new ZipkinV2Reporter(
                AsyncReporter.create(URLConnectionSender.create(v2URL))
        );
        Tracer tracer = new JaegerTracer.Builder("jaeger-zipkin")
                .withSampler(new ConstSampler(true))
                .withReporter(reporter)
                .build();

        Span span = tracer.buildSpan("zipkin-span").start();
        span.finish();
        tracer.close();
    }
}
