package org.rsfa.auth;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.rsfa.auth.err.InvalidTokenException;
import org.rsfa.auth.jwt.*;
import org.rsfa.config.JwtConfig;
import org.rsfa.config.WebSecurityConfig;
import org.rsfa.service.InMemoryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Created by radu on 4/16/17.
 */
@RestController
public class RefreshTokenEndpoint {
  @Autowired private TokenFactory tokenFactory;
  @Autowired private JwtConfig jwtSettings;
  @Autowired private InMemoryUserService userService;
  @Autowired private TokenVerifier tokenVerifier;
  @Autowired @Qualifier("jwtHeaderTokenExtractor") private TokenExtractor tokenExtractor;

  @RequestMapping(value="/api/auth/token", method= RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
  public @ResponseBody RsfaToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM));

    RawAccessToken rawToken = new RawAccessToken(tokenPayload);
    RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey())
        .orElseThrow(() -> new InvalidTokenException());

    String jti = refreshToken.getJti();
    if (!tokenVerifier.verify(jti)) {
      throw new InvalidTokenException();
    }

    String subject = refreshToken.getSubject();
    UserDetails user = userService.loadUserByUsername(subject);

    /*
    if (user.getRoles() == null) throw new InsufficientAuthenticationException("User has no roles assigned");
    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(authority -> new SimpleGrantedAuthority(authority.getRole().authority()))
        .collect(Collectors.toList());
        */

    UserContext userContext = UserContext.create(user.getUsername(), Collections.EMPTY_LIST);

    return tokenFactory.createAccessJwtToken(userContext);
  }
}
