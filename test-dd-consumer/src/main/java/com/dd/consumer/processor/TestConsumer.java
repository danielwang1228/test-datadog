package com.dd.consumer.processor;

import com.dd.common.DDRequest;
import com.dd.common.DDResponseOne;
import com.dd.common.DDResponseTwo;
import com.dd.common.GlobalConfig;
import com.dd.common.entity.UserInfo;
import com.dd.consumer.repo.UserInfoRepo;
import io.micrometer.observation.Observation;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;


@Configuration
public class TestConsumer {
    @Autowired
    private UserInfoRepo userInfoRepo;

    Tracer tracer = GlobalOpenTelemetry.getTracer("TestConsumer", "0.0.1");
    @Bean
    public Consumer<Message<DDRequest>> testConsume() {
        return message -> {
            Span span = tracer.spanBuilder("testConsumeSpan").startSpan();

            try (Scope scope = span.makeCurrent()) {
                long begin = System.currentTimeMillis() / 1000;
                System.out.println("Begin wait testConsume - " + GlobalConfig.testConsumeDelay + " : " + begin);
                TimeUnit.MILLISECONDS.sleep(GlobalConfig.testConsumeDelay);
                long end = System.currentTimeMillis() / 1000;
                System.out.println("End wait testConsume : " + end + " - " + (end - begin));
                DDRequest request = message.getPayload();
                span.setAttribute("MsgTestConsume", request.getName());
                UserInfo userInfo = new UserInfo();
                BeanUtils.copyProperties(request, userInfo);
                System.out.println("testConsume - Receive a request:" + request);
                userInfoRepo.save(userInfo);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                span.end();
            }
        };
    }

    @Bean
    public Function<Message<DDRequest>, Message<?>> testFunction() {
        return message -> {
            Span span = tracer.spanBuilder("testFunctionSpan").startSpan();

            try (Scope scope = span.makeCurrent()) {
                long begin = System.currentTimeMillis() / 1000;
                System.out.println("Begin wait testFunction - " + GlobalConfig.testFunctionDelay + " : " + begin);
                TimeUnit.MILLISECONDS.sleep(GlobalConfig.testFunctionDelay);
                long end = System.currentTimeMillis() / 1000;
                System.out.println("End wait testFunction : " + end + " - " + (end - begin));

                DDRequest request = message.getPayload();
                System.out.println("testFunction - Receive a request:" + request);

                UserInfo userInfo = new UserInfo();
                BeanUtils.copyProperties(request, userInfo);
                // access DB
                userInfoRepo.findById(request.getId()).ifPresent(u -> userInfo.setAge(u.getAge()));
                userInfoRepo.save(userInfo);
                span.setAttribute("MsgTestFunction", userInfo.getName());

                MessageBuilder<?> messageBuilder;
                if (request.getFlag() % 2 == 1) {
                    DDResponseOne responseOne = new DDResponseOne();
                    BeanUtils.copyProperties(request, responseOne);
                    messageBuilder = MessageBuilder.withPayload(responseOne);
                } else {
                    DDResponseTwo ddResponseTwo = new DDResponseTwo();
                    BeanUtils.copyProperties(request, ddResponseTwo);
                    ddResponseTwo.setNickname(request.getName());
                    messageBuilder = MessageBuilder.withPayload(ddResponseTwo);
                }
                Message<?> retMsg = messageBuilder.setHeader("header1", "headerValue1")
                        .setHeader("sendTime", System.currentTimeMillis())
                        .build();
                return retMsg;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                span.end();
            }
        };
    }
}