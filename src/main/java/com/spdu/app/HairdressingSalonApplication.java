package com.spdu.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class HairdressingSalonApplication {

    public static void main(String[] args) {
        SpringApplication.run(HairdressingSalonApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(@Value("${encoder.strength}") int strength) {
        return new BCryptPasswordEncoder(strength);
    }
}