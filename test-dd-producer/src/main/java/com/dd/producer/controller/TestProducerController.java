package com.dd.producer.controller;

import com.dd.common.DDRequest;
import com.dd.common.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("producer")
public class TestProducerController {

    @Autowired
    private StreamBridge streamBridge;

    @PostMapping("sendToTestConsume")
    public String sendToTestConsume(@RequestBody DDRequest request) {
        streamBridge.send("testSupplier-out-0", request);
        return "SUCCESS";
    }

    @PostMapping("sendToTestFunction")
    public String sendToTestFunction(@RequestBody DDRequest request) {
        streamBridge.send("testSupplierFunc-out-0", request);
        return "SUCCESS";
    }

    @GetMapping("receiveResponseDelay/{delay}")
    public String configReceiveResponseDelay(@PathVariable("delay") Long delay) {
        GlobalConfig.receiveResponseDelay = delay;
        return "SUCCESS";
    }

//    @PostMapping("jmsSendToTestConsume")
//    public String jmsSendToTestConsume(@RequestBody DDRequest request) {
//
//        streamBridge.send("testSupplier-out-0", request);
//        return "SUCCESS";
//    }
}