package com.dd.web.controller;

import com.dd.common.DDRequest;
import com.dd.web.service.LionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lion")
public class LionController {
    @Resource
    private LionService lionService;

    @PostMapping("eat")
    public String eat(@RequestBody DDRequest request) {
        return lionService.lionEat(request);
    }

    @PostMapping("run")
    public String run(@RequestBody DDRequest request) {
        return lionService.lionRun(request);
    }
}
