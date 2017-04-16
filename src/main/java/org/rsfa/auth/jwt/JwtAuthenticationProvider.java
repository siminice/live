package org.rsfa.auth.jwt;

/**
 * Created by radu on 4/15/17.
 */

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.rsfa.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {
  private final JwtConfig jwtSettings;

  @Autowired
  public JwtAuthenticationProvider(JwtConfig jwtSettings) {
    this.jwtSettings = jwtSettings;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    RawAccessToken rawAccessToken = (RawAccessToken) authentication.getCredentials();

    Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
    String subject = jwsClaims.getBody().getSubject();
    List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
    List<GrantedAuthority> authorities = scopes.stream()
        .map(authority -> new SimpleGrantedAuthority(authority))
        .collect(Collectors.toList());

    UserContext context = UserContext.create(subject, authorities);

    return new AuthenticationToken(context, context.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (AuthenticationToken.class.isAssignableFrom(authentication));
  }
}

