package org.u2soft.billtasticbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BilltasticBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BilltasticBackendApplication.class, args);
    }

}
