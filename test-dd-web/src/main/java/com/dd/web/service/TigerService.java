package com.dd.web.service;

import com.dd.common.DDRequest;
import org.springframework.stereotype.Service;

@Service
public class TigerService {
    public String tigerEat(DDRequest request) {
        return this.getClass().getName() + " eat";
    }

    public String tigerRun(DDRequest request) {
        return this.getClass().getName() + " run";
    }
}
