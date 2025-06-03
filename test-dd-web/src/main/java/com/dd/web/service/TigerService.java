package com.dd.web.service;

import com.dd.common.DDRequest;
import org.springframework.stereotype.Service;

@Service
public class TigerService {
    private OtherService otherService = new OtherService();

    public String tigerEat(DDRequest request) {
        otherService.otherRun();
        return this.getClass().getName() + " eat";
    }

    public String tigerRun(DDRequest request) {
        otherService.otherRun();
        return this.getClass().getName() + " run";
    }
}
