package com.cocola.gpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class GptFirstApplication {

    public static void main(String[] args) {
        SpringApplication.run(GptFirstApplication.class, args);
    }

}
