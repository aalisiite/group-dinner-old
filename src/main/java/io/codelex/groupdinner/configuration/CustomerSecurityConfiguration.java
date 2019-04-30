package io.codelex.groupdinner.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static io.codelex.groupdinner.repository.service.Role.REGISTERED_CLIENT;

@Order(100)
@Configuration
class CustomerSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/log-in", "/api/register").permitAll()
                .anyRequest().hasRole(REGISTERED_CLIENT.name());
    }
}
