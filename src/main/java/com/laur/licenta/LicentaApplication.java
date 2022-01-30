package com.laur.licenta;

import com.laur.licenta.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class LicentaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicentaApplication.class, args);
    }

}
