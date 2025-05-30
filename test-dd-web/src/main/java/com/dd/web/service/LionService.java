package com.dd.web.service;

import com.dd.common.DDRequest;
import org.springframework.stereotype.Service;

@Service
public class LionService {
    public String lionEat(DDRequest request) {
        return this.getClass().getName() + " eat";
    }

    public String lionRun(DDRequest request) {
        return this.getClass().getName() + " run";
    }
}
