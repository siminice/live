package org.rsfa.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

/**
 * Created by radu on 4/15/17.
 */
@SuppressWarnings("unchecked")
public class RefreshToken implements RsfaToken {
  @Getter private Jws<Claims> claims;

  private RefreshToken(Jws<Claims> claims) {
    this.claims = claims;
  }

  public static Optional<RefreshToken> create(RawAccessToken token, String signingKey) {
    Jws<Claims> claims = token.parseClaims(signingKey);

    List<String> scopes = claims.getBody().get("scopes", List.class);
    if (scopes == null || scopes.isEmpty()
        || !scopes.stream()
        .filter(scope -> Scopes.REFRESH_TOKEN.authority().equals(scope))
        .findFirst().isPresent()) {
      return Optional.empty();
    }

    return Optional.of(new RefreshToken(claims));
  }

  @Override
  public String getToken() {
    return null;
  }

  public String getJti() {
    return claims.getBody().getId();
  }

  public String getSubject() {
    return claims.getBody().getSubject();
  }
}

