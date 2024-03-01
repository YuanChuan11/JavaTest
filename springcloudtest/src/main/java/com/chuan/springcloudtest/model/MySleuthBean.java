package com.chuan.springcloudtest.model;

import brave.TracingCustomizer;
import brave.propagation.CurrentTraceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author chuanjiang
 */
@Component
public class MySleuthBean {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // 定制Trace的属性, 通过TracingCustomizer
    @Bean
    public TracingCustomizer tracingCustomizer() {
        return builder -> builder.localServiceName("changed-in-tracing-customizer");
    }

    // 通过CurrentTraceContext, 通过CurrentTraceContext.ScopeDecorator
    @Bean
    public CurrentTraceContext.ScopeDecorator scopeDecorator() {
        return (traceContext, scope) -> {
            System.out.println(traceContext + " in user defined scope decorator");
            return scope;
        };
    }
}
