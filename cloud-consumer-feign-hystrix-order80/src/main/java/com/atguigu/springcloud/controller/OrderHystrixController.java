package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@DefaultProperties(defaultFallback = "defaultFallbackMethod")
public class OrderHystrixController {
    @Autowired
    private PaymentHystrixService paymentHystrixService;

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String success(@PathVariable("id") Integer id) {
        return paymentHystrixService.success(id);
    }

    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
//    @HystrixCommand(fallbackMethod = "timeoutFallbackMethod",commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1500")
//    })
    @HystrixCommand
    public String timeout(@PathVariable("id") Integer id) {
        int i = 10/0;
        return paymentHystrixService.timeout(id);
    }

    public String timeoutFallbackMethod(@PathVariable("id") Integer id) {
        return "我是消费者80，系统繁忙，请稍后再试！！!";
    }

    //全局降级处理方法
    public String defaultFallbackMethod() {
        return "我是全局消费者80，系统繁忙，请稍后再试！！!";
    }
}
