package org.rsfa.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Created by radu on 3/27/17.
 */
public class LiveApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[] {LiveApplicationConfig.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class<?>[]{RepositoryRestMvcConfiguration.class, WebMvcAutoConfiguration.class};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[] { "/" };
  }
}
