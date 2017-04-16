package org.rsfa.auth.err;

import org.rsfa.auth.jwt.RsfaToken;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by radu on 4/15/17.
 */
public class JwtExpiredTokenException
    extends AuthenticationException {

  private RsfaToken token;

  public JwtExpiredTokenException(String msg) {
    super(msg);
  }

  public JwtExpiredTokenException(RsfaToken token, String msg, Throwable t) {
    super(msg, t);
    this.token = token;
  }

  public String token() {
    return this.token.getToken();
  }
}