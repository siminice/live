package org.rsfa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by radu on 3/30/17.
 */
@Controller
public class HomeController {

  @RequestMapping(value = "/")
  public String index() {
    return "index";
  }

}

