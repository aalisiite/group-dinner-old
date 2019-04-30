package io.codelex.groupdinner.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import javax.servlet.Filter;

@Configuration
@EnableOAuth2Client
public class FacebookFilterConfig {


    @Bean(name = "facebookSsoFilter")
    Filter facebookSsoFilter(@Qualifier("facebookClient") AuthorizationCodeResourceDetails facebook,
                             @Qualifier("facebookResource") ResourceServerProperties facebookResource,
                             @Qualifier("facebookRestTemplate") OAuth2RestTemplate oAuth2RestTemplate) {
        OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter("/api/login/facebook");
        facebookFilter.setAuthenticationSuccessHandler(new RedirectToRoot());
        facebookFilter.setRestTemplate(oAuth2RestTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(facebookResource.getUserInfoUri(), facebook.getClientId());
        tokenServices.setRestTemplate(oAuth2RestTemplate);
        tokenServices.setPrincipalExtractor(new FacebookPrincipalExtractor());
        facebookFilter.setTokenServices(tokenServices);
        return facebookFilter;
    }

    @Bean(name = "facebookClient")
    @ConfigurationProperties("facebook.client")
    AuthorizationCodeResourceDetails facebook() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean(name = "facebookResource")
    @ConfigurationProperties("facebook.resource")
    ResourceServerProperties facebookResource() {
        return new ResourceServerProperties();
    }

    @Bean(name = "facebookRestTemplate")
    OAuth2RestTemplate facebookOAuth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
                                                  @Qualifier("facebookClient") AuthorizationCodeResourceDetails facebook) {
        return new OAuth2RestTemplate(facebook, oauth2ClientContext);
    }

    @Bean
    FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

}
