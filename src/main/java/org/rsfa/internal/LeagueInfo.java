package org.rsfa.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Created by radu on 3/27/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeagueInfo {
  @Id
  protected String id = "";
  private String country = "";
  private String name = "";
  private String group = "";
  private String tier = "";
  private String number = "";
}
