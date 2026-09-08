package com.memorylink.config;
import com.memorylink.service.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class DataInitializer {
 @Bean CommandLineRunner init(StudentService service){return args -> service.createAdmin("admin","admin123");}
}
