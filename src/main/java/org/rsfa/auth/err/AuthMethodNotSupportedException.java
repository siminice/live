package org.rsfa.auth.err;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * Created by radu on 4/15/17.
 */
public class AuthMethodNotSupportedException extends AuthenticationServiceException {

  public AuthMethodNotSupportedException(String msg) {
    super(msg);
  }
}