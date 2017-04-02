package org.rsfa.config;

import org.rsfa.service.InMemoryLeagueService;
import org.rsfa.service.LeagueService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by radu on 3/28/17.
 */
@Configuration
public class LeagueServiceConfig {
  @Bean
  public LeagueService leagueService() { return new InMemoryLeagueService(); }
}
