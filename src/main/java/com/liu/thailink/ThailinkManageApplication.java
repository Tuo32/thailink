package com.liu.thailink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;


@EnableTransactionManagement
@SpringBootApplication
@RestController
//Avoid cross domain issues
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ThailinkManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThailinkManageApplication.class, args);
    }


}
