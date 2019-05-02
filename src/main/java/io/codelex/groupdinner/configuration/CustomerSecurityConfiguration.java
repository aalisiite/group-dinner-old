package io.codelex.groupdinner.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import static io.codelex.groupdinner.repository.service.Role.USER;

@Order(100)
@Configuration
@EnableOAuth2Client
class CustomerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/log-in", "/api/register").permitAll()
                .anyRequest().hasAnyRole(USER.name(), "USER")
                .and().logout().logoutUrl("/api/log-out");
    }


}



