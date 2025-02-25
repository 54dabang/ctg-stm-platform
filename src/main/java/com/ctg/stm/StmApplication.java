package com.ctg.stm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class StmApplication {

    private static final Logger logger = LoggerFactory.getLogger(StmApplication.class);

    @Value("${env}")
    private String env;

    @PostConstruct
    public void startupApplication() {
        logger.warn("Using environment: " + env);
    }

    public static void main(String[] args) {
        SpringApplication.run(StmApplication.class, args);
    }

}
