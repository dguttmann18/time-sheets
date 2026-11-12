package com.psybergate.mentoring.cloud.time.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class LeaveClientConfig {

  @Bean
  public RestClient leaveRestClient(@Value("${leave.service.base-url}") String baseUrl) {
    return RestClient.builder()
            .baseUrl(baseUrl)
            .build();
  }
}
