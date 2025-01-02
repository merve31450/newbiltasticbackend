package org.u2soft.billtasticbackend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Tüm endpoint'ler için
                        .allowedOrigins("http://localhost:3000") // İzin verilen kaynak
                        .allowedMethods("*") // GET, POST, PUT, DELETE vb. tüm metodlara izin
                        .allowedHeaders("*") // Tüm başlıklara izin
                        .allowCredentials(true); // Kimlik bilgilerini destekler
            }

        };
    }
}


