package com.shawn.notification.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class OpenFeignSlackConfig {

    @Value("${slack.botOauthToken}")
    private String botOauthToken;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("Authorization", "Bearer " + botOauthToken);
    }
}
