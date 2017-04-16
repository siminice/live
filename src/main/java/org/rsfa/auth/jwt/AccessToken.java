package org.rsfa.auth.jwt;

import io.jsonwebtoken.Claims;

/**
 * Created by radu on 4/15/17.
 */
public final class AccessToken implements RsfaToken {
    private final String rawToken;
    private Claims claims;

    protected AccessToken(final String token, Claims claims) {
      this.rawToken = token;
      this.claims = claims;
    }

    public String getToken() {
      return this.rawToken;
    }

    public Claims getClaims() {
      return claims;
    }
}
