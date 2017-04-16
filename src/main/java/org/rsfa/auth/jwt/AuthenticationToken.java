package org.rsfa.auth.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by radu on 4/15/17.
 */
public class AuthenticationToken extends AbstractAuthenticationToken {

  private RawAccessToken rawAccessToken;
  private UserContext userContext;

  public AuthenticationToken(RawAccessToken unsafeToken) {
    super(null);
    this.rawAccessToken = unsafeToken;
    this.setAuthenticated(false);
  }

  public AuthenticationToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.eraseCredentials();
    this.userContext = userContext;
    super.setAuthenticated(true);
  }

  @Override
  public void setAuthenticated(boolean authenticated) {
    if (authenticated) {
      throw new IllegalArgumentException(
          "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    }
    super.setAuthenticated(false);
  }

  @Override
  public Object getCredentials() {
    return rawAccessToken;
  }

  @Override
  public Object getPrincipal() {
    return this.userContext;
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    this.rawAccessToken = null;
  }
}