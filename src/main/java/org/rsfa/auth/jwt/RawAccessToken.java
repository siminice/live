package org.rsfa.auth.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.rsfa.auth.err.JwtExpiredTokenException;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * Created by radu on 4/15/17.
 */
@Slf4j
public class RawAccessToken implements RsfaToken {

  private String token;

  public RawAccessToken(String token) {
    this.token = token;
  }

  public Jws<Claims> parseClaims(String signingKey) {
    try {
      return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
    } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
      log.error("Invalid JWT Token", ex);
      throw new BadCredentialsException("Invalid JWT token: ", ex);
    } catch (ExpiredJwtException expiredEx) {
      log.info("JWT Token is expired", expiredEx);
      throw new JwtExpiredTokenException(this, "JWT Token expired", expiredEx);
    }
  }

  @Override
  public String getToken() {
    return token;
  }
}

