package com.beautysalon.beautysalonbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.beautysalon.beautysalonbooking",
        "com/beautysalon/beautysalonbooking",
        "com/beautysalon/beautysalonbooking",
        "com/beautysalon/beautysalonbooking"
})
public class BeautySalonBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeautySalonBookingApplication.class, args);
    }
}