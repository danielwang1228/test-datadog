package com.dd.consumer.controller;

import com.dd.common.GlobalConfig;
import com.dd.common.entity.UserInfo;
import com.dd.consumer.repo.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("consumer")
public class TestConsumerController {
    @Autowired
    private UserInfoRepo userInfoRepo;

    @GetMapping("getAll")
    public List<UserInfo> getAll() {
        return userInfoRepo.findAll();
    }

    @GetMapping("testConsumeDelay/{delay}")
    public String configTestConsumeDelay(@PathVariable("delay") Long delay) {
        GlobalConfig.testConsumeDelay = delay;
        return "SUCCESS";
    }

    @GetMapping("testFunctionDelay/{delay}")
    public String configTestFunctionDelay(@PathVariable("delay") Long delay) {
        GlobalConfig.testFunctionDelay = delay;
        return "SUCCESS";
    }
}
