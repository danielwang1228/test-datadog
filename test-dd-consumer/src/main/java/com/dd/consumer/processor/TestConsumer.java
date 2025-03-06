package com.dd.consumer.processor;

import com.dd.common.DDRequest;
import com.dd.common.DDResponse;
import com.dd.common.entity.UserInfo;
import com.dd.consumer.repo.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class TestConsumer {
    @Autowired
    private UserInfoRepo userInfoRepo;

    @Bean
    public Consumer<Message<DDRequest>> testConsume() {
        return message -> {
            DDRequest request = message.getPayload();
            UserInfo userInfo = new UserInfo();
            userInfo.setId(request.getId());
            userInfo.setName(request.getName());
            userInfo.setAge(request.getAge());
            System.out.println("testConsume - Receive a request:" + request);
            userInfoRepo.save(userInfo);
        };
    }

    @Bean
    public Function<Message<DDRequest>, Message<DDResponse>> testFunction() {
        return message -> {
            DDRequest request = message.getPayload();
            DDResponse response = new DDResponse();
            response.setId(request.getId());
            response.setName(request.getName() + "-xxxxxx");
            response.setAddress(request.getName() + "-address");
            System.out.println("testFunction - Receive a request:" + request);
            UserInfo userInfo = new UserInfo();
            userInfo.setId(request.getId());
            userInfo.setName(response.getName());
            userInfo.setAddress(response.getAddress());
            Optional<UserInfo> userInfoOptional = userInfoRepo.findById(request.getId());
            if (userInfoOptional.isPresent()) {
                userInfo.setAge(userInfoOptional.get().getAge());
            }
            userInfoRepo.save(userInfo);
            return MessageBuilder.withPayload(response)
                    .setHeader("header1", "headerValue1")
                    .setHeader("sendTime", System.currentTimeMillis())
                    .build();
        };
    }
}