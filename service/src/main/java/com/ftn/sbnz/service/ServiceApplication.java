package com.ftn.sbnz.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.ftn.sbnz.service.demo.DroolsConsoleDemo;

@SpringBootApplication
public class ServiceApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        DroolsConsoleDemo.runDemo();
    }
}