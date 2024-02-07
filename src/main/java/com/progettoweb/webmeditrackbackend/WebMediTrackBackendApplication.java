package com.progettoweb.webmeditrackbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ServletComponentScan
public class WebMediTrackBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebMediTrackBackendApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/doRegistering").allowedOrigins("http://localhost:4200").allowCredentials(true);
                registry.addMapping("/doLogging").allowedOrigins("http://localhost:4200").allowCredentials(true);
                registry.addMapping("/search/medicine").allowedOrigins("http://localhost:4200").allowCredentials(true);
                registry.addMapping("/search/patient").allowedOrigins("http://localhost:4200").allowCredentials(true);
                registry.addMapping("/search/plan").allowedOrigins("http://localhost:4200").allowCredentials(true);
                registry.addMapping("/update/user").allowedOrigins("http://localhost:4200").allowCredentials(true);
                registry.addMapping("/update/addPatient").allowedOrigins("http://localhost:4200").allowCredentials(true);
                registry.addMapping("/update/removePatient").allowedOrigins("http://localhost:4200").allowCredentials(true);
                registry.addMapping("/update/addPlan").allowedOrigins("http://localhost:4200").allowCredentials(true);
                registry.addMapping("/update/removePlan").allowedOrigins("http://localhost:4200").allowCredentials(true);
                registry.addMapping("/update/plan/addMedicine").allowedOrigins("http://localhost:4200").allowCredentials(true);
                registry.addMapping("/update/plan/removeMedicine").allowedOrigins("http://localhost:4200").allowCredentials(true);
                registry.addMapping("/update/plan/data").allowedOrigins("http://localhost:4200").allowCredentials(true);
            }
        };
    }

}
