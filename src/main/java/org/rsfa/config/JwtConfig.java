package org.rsfa.config;

/**
 * Created by radu on 4/15/17.
 */

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtConfig {

  @Getter @Setter
  private Integer tokenExpirationTime;

  @Getter @Setter
  private String tokenIssuer;

  @Getter @Setter
  private String tokenSigningKey;

  @Getter @Setter
  private Integer refreshTokenExpTime;

}

