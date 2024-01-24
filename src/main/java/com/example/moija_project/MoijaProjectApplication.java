package com.example.moija_project;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
@EnableAspectJAutoProxy
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.moija_project.repository")
@EnableMongoRepositories(basePackages = "com.example.moija_project.mongo")
public class MoijaProjectApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MoijaProjectApplication.class, args);
    }
    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }


}
