package com.chuan.springcloudtest.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author chuanjiang
 */
@SpringBootApplication
@ComponentScan({"com.chuan.springcloudtest"})
@RestController
@Slf4j
public class SleuthApplication {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Greeting greeting;

    @RequestMapping("/")
    public String home() {
        log.info("Hello Sleuth!");
        // sleuth框架会自动传播traceId和spanId
        String result = restTemplate.getForObject("http://localhost:8080/hi", String.class);
        log.info("result: {}", result);
        return "Hello Sleuth!";
    }

    @RequestMapping("/hi")
    public String hi() {
        log.info("handling hi!");
        String result = greeting.methodSpan("is me");
        log.info("result: {}", result);
        return "hi!";
    }

    @Component
    class Greeting {
        @NewSpan("method-span")
        public String methodSpan(@SpanTag("method-span") String whom) {
            log.info("handling methodSpan!" + whom);
            return "methodSpan!" + whom;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SleuthApplication.class, args);
    }
}
