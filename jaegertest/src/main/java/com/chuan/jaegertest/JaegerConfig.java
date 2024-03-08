package com.chuan.jaegertest;

import io.jaegertracing.Configuration;

public class JaegerConfig {

    public static Configuration GetConfiguration(String serviceName) {
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
