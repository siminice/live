package org.rsfa.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by radu on 4/15/17.
 */
@Component
public class InMemoryUserService implements UserDetailsService {
  private Map<String, User> userDb = new HashMap<>();

  public InMemoryUserService() {
    try {
      init();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void init() throws IOException {
    userDb.put("radu", new User("radu", "lcbGGgIr2N2HTh2J6WxkYObPYySRhweo3BObHiVROKo=",
        AuthorityUtils.createAuthorityList("USER_ROLE")));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userDb.get(username);
    if (user == null) {
      throw new UsernameNotFoundException(String.format("User {} not found" , username));
    }
    return user;
  }
}
