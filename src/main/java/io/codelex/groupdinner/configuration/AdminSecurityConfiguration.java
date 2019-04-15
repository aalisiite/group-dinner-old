package io.codelex.groupdinner.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static io.codelex.groupdinner.repository.service.Role.ADMIN;

@Order(200)
@Configuration
class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/admin-api/**")
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/sign-in").permitAll()
                .anyRequest().hasRole(ADMIN.name());
    }
}
