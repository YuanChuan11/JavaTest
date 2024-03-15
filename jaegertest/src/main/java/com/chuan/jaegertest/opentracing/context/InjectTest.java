package com.chuan.jaegertest.opentracing.context;

import com.chuan.jaegertest.config.JaegerConfig;
import io.jaegertracing.Configuration;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chuanjiang
 */
public class InjectTest {

    // 将span上下文注入到map中
    public static void injectContext2Map() {
        Configuration configuration = JaegerConfig.getConfiguration("inject-demo");

        Tracer tracer = configuration.getTracer();
        Span span = tracer.buildSpan("inject-span").start();
        span.setBaggageItem("baggage", "baggage-value");
        span.setBaggageItem("baggage2", "baggage-value2");
        Map<String, String> map = new HashMap<>();

        // 随行数据会扩散注入到map中
        tracer.inject(span.context(), Format.Builtin.TEXT_MAP, new TextMapAdapter(map));

        // 默认输出格式是jaeger默认前缀 {uberctx-baggage=baggage-value,
        // uberctx-baggage2=baggage-value2, uber-trace-id=e66ef9ac3e13e045:e66ef9ac3e13e045:0:1}
        System.out.println(map);

        span.finish();
        tracer.close();
    }

    public static void main(String[] args) {
        injectContext2Map();
    }
}
