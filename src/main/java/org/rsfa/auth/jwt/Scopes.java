package org.rsfa.auth.jwt;

/**
 * Created by radu on 4/15/17.
 */
public enum Scopes {
  REFRESH_TOKEN;

  public String authority() {
    return "ROLE_" + this.name();
  }
}
