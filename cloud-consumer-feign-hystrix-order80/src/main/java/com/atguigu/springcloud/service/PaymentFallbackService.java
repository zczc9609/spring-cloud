package com.atguigu.springcloud.service;

import org.springframework.stereotype.Component;

@Component
public class PaymentFallbackService implements PaymentHystrixService{


    @Override
    public String success(Integer id) {
        return "请求失败！！！";
    }

    @Override
    public String timeout(Integer id) {
        return  "请求失败！！！";
    }
}
