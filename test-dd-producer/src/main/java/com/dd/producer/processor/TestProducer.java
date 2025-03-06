package com.dd.producer.processor;

import com.dd.common.DDRequest;
import com.dd.common.DDResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class TestProducer {
    @Bean
    public Consumer<Message<DDResponse>> receiveResponse() {
        return message -> {
            DDResponse response = message.getPayload();
            System.out.println("receiveResponse - Receive a response:" + response);
        };
    }
}
