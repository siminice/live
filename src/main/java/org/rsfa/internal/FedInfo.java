package org.rsfa.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by radu on 3/27/17.
 */
@Data
@AllArgsConstructor
public class FedInfo {
  @Id
  protected String name;
  private String script;
  private String dir;
  private List<LeagueInfo> leagues;

  public void addLeague(LeagueInfo lg) {
    List<LeagueInfo> current = getLeagues();
    current.add(lg);
    setLeagues(current);
  }
}
