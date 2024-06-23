package hieutran.crud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//! Đây là cách một để câu hính cors trong Spring Boot bằng implement WebMvcConfigurer
//@Configuration
//public class AppConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowCredentials(true)
//                .allowedOrigins("http://localhost:5500/", "http://127.0.0.1:5500/")
//                .allowedMethods("*")
//                .allowedMethods("*");
//    }
//}
