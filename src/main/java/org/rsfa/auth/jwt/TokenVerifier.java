package org.rsfa.auth.jwt;

/**
 * Created by radu on 4/15/17.
 */
public interface TokenVerifier {
  public boolean verify(String jti);
}
