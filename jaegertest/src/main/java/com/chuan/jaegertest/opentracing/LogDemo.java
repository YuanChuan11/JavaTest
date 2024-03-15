package com.chuan.jaegertest.opentracing;

import com.chuan.jaegertest.config.JaegerConfig;
import io.jaegertracing.Configuration;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.log.Fields;

import java.util.Collections;

/**
 * @author chuanjiang
 */
public class LogDemo {
    public static void main(String[] args) throws InterruptedException {
        Configuration configuration = JaegerConfig.getConfiguration("LogDemo");
        Tracer tracer = configuration.getTracer();
        Span span = tracer.buildSpan("log-span").start();
        span.log("start");
        span.log(System.currentTimeMillis() * 1000L + 500 * 1000, "after start");
        Thread.sleep(1000);
        span.log(Collections.singletonMap(Fields.MESSAGE, "hello"));
        span.log(System.currentTimeMillis() * 1000L + 500 * 1000, "after message");
        Thread.sleep(1000);
        span.log("end");
        span.finish();
        tracer.close();
    }
}
