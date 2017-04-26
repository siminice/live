package org.rsfa.config;

import org.rsfa.service.InMemoryReportService;
import org.rsfa.service.ReportService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by radu on 4/26/17.
 */
@Configuration
public class ReportServiceConfig {
  @Bean
  ReportService reportService() { return new InMemoryReportService(); }
}
