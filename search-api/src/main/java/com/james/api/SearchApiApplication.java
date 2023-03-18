package com.james.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SearchApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApiApplication.class, args);
    }
}
