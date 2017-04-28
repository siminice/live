package org.rsfa.model.catalog;

import lombok.Data;

/**
 * Created by radu on 4/26/17.
 */
@Data
public class Cap {
  private String name = "";
  private int min = 0;

  public static Cap from(String s) {
    Cap cap = new Cap();
    if (s != null) {
      String tk[] = s.split(":");
      if (tk.length>=2) {
        cap.setName(tk[0].trim());
        cap.setMin(Integer.parseInt(tk[1].trim()));
      }
    }
    return cap;
  }

  @Override
  public String toString() {
    return String.format("%s:%-3d", name, min);
  }

  public static final Cap UNKNOWN = new Cap();
}
