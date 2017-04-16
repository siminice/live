package org.rsfa.auth.jwt;

import org.springframework.stereotype.Component;

/**
 * Created by radu on 4/15/17.
 */
@Component
public class RsfaTokenVerifier implements TokenVerifier {
  @Override
  public boolean verify(String jti) {
    return true;
  }
}
