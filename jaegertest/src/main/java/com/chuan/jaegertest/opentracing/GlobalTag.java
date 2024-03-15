package com.chuan.jaegertest.opentracing;

import com.chuan.jaegertest.config.JaegerConfig;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerSpan;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Span;
import io.opentracing.Tracer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chuanjiang
 */
public class GlobalTag {

    public static void main(String[] args) {
        test2();
    }

    public static void test1() {
        Map<String, String> tags = new HashMap<>();
        tags.put("map_string_tag", "map_string_value");
        tags.put("map_number_tag", "2.71828");
        tags.put("map_bool_tag", "false");
        JaegerTracer tracer = new JaegerTracer.Builder("demo_for_process_tags")
                .withSampler(new ConstSampler(true))
                .withTags(tags)
                .withTag("string_tag", "string_value")
                .withTag("number_tag", 3.14159)
                .withTag("bool_tag", false)
                .build();
        Span span = tracer.buildSpan("process_tags_span").start();
        System.out.println(((JaegerSpan) span).getTags());
        span.finish();
        tracer.close();
    }

    public static void test2() {
        Map<String, String> tags = new HashMap<>();
        tags.put("map_string_tag", "map_string_value");
        tags.put("map_number_tag", "2.71828");
        tags.put("map_bool_tag", "false");
        Configuration configuration = JaegerConfig.getConfiguration("demo_for_process_tags").withTracerTags(tags);
        Tracer tracer = configuration.getTracer();
        Span span = tracer.buildSpan("process_tags_span").start();
        System.out.println(((JaegerSpan) span).getTags());
        span.finish();
        tracer.close();
    }

}
