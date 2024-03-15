package com.chuan.jaegertest.config;

import io.jaegertracing.Configuration;

public class JaegerConfig {

    public static Configuration getConfiguration(String serviceName) {
        Configuration.ReporterConfiguration reporterConfiguration = getReportConfig();
        Configuration.SamplerConfiguration samplerConfiguration = getSamplerConfig();
        Configuration.CodecConfiguration codecConfiguration = getCodecConfig();

        // 创建配置 可通过配置生产tracer对象
        return Configuration.fromEnv(serviceName)
                .withSampler(samplerConfiguration)
                .withReporter(reporterConfiguration)
                .withCodec(codecConfiguration);
    }

    public static Configuration.ReporterConfiguration getReportConfig() {

        // 发送组件 配置发送的地址 agent端口是6831(代理组件端口:5778??)  http collector端口是14268
        Configuration.SenderConfiguration senderConfiguration = Configuration.SenderConfiguration.fromEnv()
                .withAgentHost("node1")
                .withAgentPort(6831);
        //      .withEndpoint("http://node1:14268/api/traces");

        // 控制上报配置, 包括上报间隔, 最大队列, 是否打印日志, 发送组件
        Configuration.ReporterConfiguration reporterConfiguration = Configuration.ReporterConfiguration.fromEnv()
                .withFlushInterval(2000)
                .withMaxQueueSize(10)
                .withLogSpans(true)
                .withSender(senderConfiguration);

        return reporterConfiguration;
    }

    public static Configuration.SamplerConfiguration getSamplerConfig() {
        // 采样配置, 采样类型, 采样参数
        return Configuration.SamplerConfiguration.fromEnv()
                .withType("const")
                .withParam(1);
    }

    /**
     * 传播span上下文时的协议,
     * 默认是jaeger格式(uber-开头)
     * 可设置为w3c格式(traceparent=00-0000000000000000a0697c5c67d8c9b4-a0697c5c67d8c9b4-01)
     * 可设置为b3格式(X-B3-开头) zipkin用
     * <p>
     * withPropagation方法会自动绑定格式和协议
     *
     * @return codec
     */
    public static Configuration.CodecConfiguration getCodecConfig() {
        // 设置了环境变量再在fromEnv方法中设置时, 会同时输出两种格式, 而不是覆盖
        System.setProperty("JAEGER_PROPAGATION", "b3");
        //return Configuration.CodecConfiguration.fromEnv();
        //return Configuration.CodecConfiguration.fromString("b3");
        return Configuration.CodecConfiguration.fromEnv().withPropagation(Configuration.Propagation.W3C);
    }
}
