package org.rsfa.auth.jwt;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by radu on 4/15/17.
 */
@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {
  public static String HEADER_PREFIX = "Bearer ";

  @Override
  public String extract(String header) {
    if (StringUtils.isEmpty(header)) {
      throw new AuthenticationServiceException("Authorization header cannot be blank!");
    }

    if (header.length() < HEADER_PREFIX.length()) {
      throw new AuthenticationServiceException("Invalid authorization header size.");
    }

    return header.substring(HEADER_PREFIX.length(), header.length());
  }
}
