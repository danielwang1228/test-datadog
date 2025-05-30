package com.dd.web.controller;

import com.dd.common.DDRequest;
import com.dd.web.service.TigerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tiger")
public class TigerController {
    @Resource
    private TigerService tigerService;

    @PostMapping("eat")
    public String eat(@RequestBody DDRequest request) {
        return tigerService.tigerEat(request);
    }

    @PostMapping("run")
    public String run(@RequestBody DDRequest request) {
        return tigerService.tigerRun(request);
    }
}
