package com.alsritter.service.test2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author alsritter
 * @version 1.0
 **/
@EnableDiscoveryClient
@SpringBootApplication
public class Test2Application {
    public static void main(String[] args) {
        SpringApplication.run(Test2Application.class, args);
    }
}
