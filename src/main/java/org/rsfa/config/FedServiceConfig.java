package org.rsfa.config;

import org.rsfa.service.FedService;
import org.rsfa.service.InMemoryFedService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by radu on 3/27/17.
 */
@Configuration
public class FedServiceConfig {
  @Bean
  FedService fedService() { return new InMemoryFedService(); }
}
