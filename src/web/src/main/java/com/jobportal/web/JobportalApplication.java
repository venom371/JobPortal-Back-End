package com.jobportal.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.jobportal.*")
@EnableJpaRepositories(basePackages = "com.jobportal")
@ComponentScan(basePackages = "com.jobportal.*")
@EntityScan(basePackages = "com.jobportal")
public class JobportalApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobportalApplication.class, args);
    }

}
