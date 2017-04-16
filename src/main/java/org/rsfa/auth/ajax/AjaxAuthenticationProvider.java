package org.rsfa.auth.ajax;

import org.rsfa.auth.jwt.UserContext;
import org.rsfa.service.InMemoryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Created by radu on 4/16/17.
 */
@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {
  private final BCryptPasswordEncoder encoder;
  private final InMemoryUserService userService;

  @Autowired
  public AjaxAuthenticationProvider(final InMemoryUserService userService, final BCryptPasswordEncoder encoder) {
    this.userService = userService;
    this.encoder = encoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Assert.notNull(authentication, "No authentication data provided");

    String username = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();

    User user = (User) userService.loadUserByUsername(username);

    /*
    if (!encoder.matches(password, user.getPassword())) {
      throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
    }
    */

    if (!password.equals(user.getPassword())) {
      throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
    }

    /*
    if (user.getRoles() == null) throw new InsufficientAuthenticationException("User has no roles assigned");

    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(authority -> new SimpleGrantedAuthority(authority.getRole().authority()))
        .collect(Collectors.toList());
    */

    UserContext userContext = UserContext.create(user.getUsername(),
        Arrays.asList(new SimpleGrantedAuthority("USER_ROLE")));

    return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
}

