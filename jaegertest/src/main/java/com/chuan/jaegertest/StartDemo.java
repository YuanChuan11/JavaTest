package com.chuan.jaegertest;

import io.jaegertracing.Configuration;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * @author chuanjiang
 */
public class StartDemo {

    public static void main(String[] args) {
        demoTest();
    }

    public static void demoTest() {
        Configuration configuration = getConfiguration("jaeger-demo");

        Tracer tracer = configuration.getTracer();
        Span span = tracer.buildSpan("jaeger-span").start();

        span.finish();
        tracer.close();
    }

    public static Configuration getConfiguration(String serviceName) {
        Configuration.SenderConfiguration senderConfiguration = Configuration.SenderConfiguration.fromEnv()
                .withAgentHost("node1").withAgentPort(6831);
        Configuration.ReporterConfiguration reporterConfiguration = Configuration.ReporterConfiguration.fromEnv()
                .withFlushInterval(2000).withMaxQueueSize(10)
                .withLogSpans(true).withSender(senderConfiguration);

        Configuration.SamplerConfiguration samplerConfiguration =
                Configuration.SamplerConfiguration.fromEnv()
                        .withType("const").withParam(1);

        Configuration configuration = Configuration.fromEnv(serviceName)
                .withSampler(samplerConfiguration).withReporter(reporterConfiguration);
        return configuration;
    }

}