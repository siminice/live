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
    userDb.put("rsfa", new User("rsfa", "$2a$10$KMm3KDuuonrX0P2luGii6.bmnI4dZyvjHXjd0xXhpfTitX1Sg15MW",
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
