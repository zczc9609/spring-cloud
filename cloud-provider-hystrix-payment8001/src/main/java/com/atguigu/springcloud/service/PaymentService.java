package com.atguigu.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    public String hystrixInfo_Ok(Integer id) {
        return "线程池:  " + Thread.currentThread().getName() + "id: " + id;
    }

    @HystrixCommand(fallbackMethod = "hystrixInfo_TimeoutHandler",commandProperties = {
//            程序运行超过三秒，或者运行出错等就调用fallbackMethod返回处理结果
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    })
    public String hystrixInfo_Timeout(Integer id) {
        int time = 3;
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池:  " + Thread.currentThread().getName() + "id: " + id + "耗时: " + time;
    }

    public String hystrixInfo_TimeoutHandler(Integer id) {
        return "线程池:  " + Thread.currentThread().getName() + "\t" + "8001系统运行繁忙或运行报错，请稍后再试！  " + "id: " + id + "\t" + "❌";
    }

    //*************服务熔断
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallBack",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),      //开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),      //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),      //时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60")      //失败率达到多少跳闸
            //意思是：在10秒内请求数10次内60%失败，则拉闸
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        if(id < 0) {
            throw new RuntimeException("***id不能为负！！");
        }
        String uuid = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t" + "调用成功，流水号：  " + uuid;
    }

    public String paymentCircuitBreaker_fallBack(@PathVariable("id") Integer id) {
        return "id不能为负！！";
    }
}
