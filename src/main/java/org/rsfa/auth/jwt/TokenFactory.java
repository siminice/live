package org.rsfa.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.rsfa.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by radu on 4/15/17.
 */
@Component
public class TokenFactory {
  private final JwtConfig settings;

  @Autowired
  public TokenFactory(JwtConfig settings) {
    this.settings = settings;
  }

  public AccessToken createAccessJwtToken(UserContext userContext) {
    if (StringUtils.isEmpty(userContext.getUsername()))
      throw new IllegalArgumentException("Cannot create JWT Token without username");

    if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty())
      throw new IllegalArgumentException("User doesn't have any privileges");

    Claims claims = Jwts.claims().setSubject(userContext.getUsername());
    claims.put("scopes", userContext.getAuthorities().stream()
        .map(s -> s.toString()).collect(Collectors.toList()));

    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime expirationTime = currentTime.plusMinutes(settings.getTokenExpirationTime());

    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuer(settings.getTokenIssuer())
        .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
        .setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
        .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
        .compact();

    return new AccessToken(token, claims);
  }

  public RsfaToken createRefreshToken(UserContext userContext) {
    if (StringUtils.isEmpty(userContext.getUsername())) {
      throw new IllegalArgumentException("Cannot create JWT Token without username");
    }

    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime expirationTime = currentTime.plusMinutes(settings.getRefreshTokenExpTime());

    Claims claims = Jwts.claims().setSubject(userContext.getUsername());
    claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));

    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuer(settings.getTokenIssuer())
        .setId(UUID.randomUUID().toString())
        .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
        .setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
        .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
        .compact();

    return new AccessToken(token, claims);
  }
}
