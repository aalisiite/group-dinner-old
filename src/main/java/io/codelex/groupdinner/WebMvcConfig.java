package io.codelex.groupdinner;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

   /* @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }*/
}
