package com.james.api;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
@EnableFeignClients
@SpringBootApplication
public class SearchApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApiApplication.class, args);
    }
}
