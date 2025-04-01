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
        return message -> {
            try {
                long begin = System.currentTimeMillis() / 1000;
                System.out.println("Begin wait receiveResponse - " + GlobalConfig.receiveResponseDelay + " : " + begin);
                TimeUnit.MILLISECONDS.sleep(GlobalConfig.receiveResponseDelay);
                long end = System.currentTimeMillis() / 1000;
                System.out.println("End wait receiveResponse : " + end + " - "  + (end - begin));


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String response = message.getPayload();
            System.out.println("receiveResponse - Receive a response:" + response);
        };
    }

}
