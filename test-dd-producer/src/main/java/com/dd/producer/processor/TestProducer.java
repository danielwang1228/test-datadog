package com.dd.producer.processor;

import com.dd.common.DDResponseOne;
import com.dd.common.DDResponseTwo;
import com.dd.common.GlobalConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Configuration
public class TestProducer {
    @Bean
    public Consumer<Message<String>> receiveResponse() {
        try {
            TimeUnit.MILLISECONDS.sleep(GlobalConfig.receiveResponseDelay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return message -> {
            String response = message.getPayload();
            System.out.println("receiveResponse - Receive a response:" + response);
        };
    }

}
