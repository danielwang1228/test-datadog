package com.dd.consumer.controller;

import com.dd.common.entity.UserInfo;
import com.dd.consumer.repo.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
