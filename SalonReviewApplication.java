package com.salon.review.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.salon.review")
public class SalonReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalonReviewApplication.class, args);
    }
}
