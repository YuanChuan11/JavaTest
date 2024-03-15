package com.chuan.jaegertest.jsdk;

import io.jaegertracing.Configuration;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * 代码详细注释参考 {@link com.chuan.jaegertest.config.JaegerConfig#getConfiguration(String)}
 *
 * @author chuanjiang
 */
public class ReporterDemo {

    /**
     * @see io.jaegertracing.spi.Reporter
     * <p>
     * RemoteReporter  上报到远程地址
     * LogReporter  打印到日志
     * CompositeReporter  组合多个Reporter，会依次调用其report方法
     * InMemoryReporter 保存到内存
     * NoopReporter 空操作
     */
    public static void remoteReporterTest() {
        Configuration.SenderConfiguration senderConfiguration = Configuration.SenderConfiguration.fromEnv()
                .withEndpoint("http://node1:14268/api/traces");

        Configuration.ReporterConfiguration reporterConfiguration = Configuration.ReporterConfiguration.fromEnv()
                .withFlushInterval(2000)
                .withMaxQueueSize(10)
                .withSender(senderConfiguration)
                //.withLogSpans(true); 会生成CompositeReporter里面包含了 LoggingReporter 和 RemoteReporter
                .withLogSpans(true);

        Configuration.SamplerConfiguration samplerConfiguration = Configuration.SamplerConfiguration.fromEnv()
                .withType("const")
                .withParam(1);

        Configuration configuration = Configuration.fromEnv("remote-reporter-demo")
                .withReporter(reporterConfiguration)
                .withSampler(samplerConfiguration);

        Tracer tracer = configuration.getTracer();
        Span span = tracer.buildSpan("collection-span").start();
        span.finish();
        tracer.close();
    }

    public static void main(String[] args) {
        remoteReporterTest();
    }
}
