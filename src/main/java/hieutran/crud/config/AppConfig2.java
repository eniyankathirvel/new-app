package hieutran.crud.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

//@Configuration
//public class AppConfig2 {
//    //! Cách 2 để cấu hình CORS trong Spring Boot bằng bean
////    @Bean
////    public WebMvcConfigurer corsFilter() {
////        return new WebMvcConfigurer() {
////            @Override
////            public void addCorsMappings(CorsRegistry registry) {
////                registry.addMapping("/**")
////                        .allowCredentials(true)
////                        .allowedOrigins("http://localhost:5500/", "http://127.0.0.1:5500/");
////            }
////        };
////    }
//    //! Cách 3 câu hình cors trong Spring Boot bằng crosFilter
//    @Bean
//    public FilterRegistrationBean<CorsFilter> corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
////        config.addAllowedOrigin("http://127.0.0.1:5500/"); //! cho phép 1 domain này truy cập
//        config.setAllowedOrigins(List.of("http://127.0.0.1:5500/", "http://localhost:5500/")); //! cho phép nhiều domain truy cập(Tương tự nhưng cái khác cũng vậy như setAllowedMethod)
//        config.addAllowedMethod("*");
//        config.addAllowedHeader("*");
//        source.registerCorsConfiguration("/**", config); //cho phép tất cả domain truy cập
//        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return bean;
//    }
//}
