package com.example.project_rent_yard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjectRentYardApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectRentYardApplication.class, args);
    }

}
