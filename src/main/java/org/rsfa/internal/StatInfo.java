package org.rsfa.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rsfa.model.stats.Stat;

import java.util.Arrays;
import java.util.List;

/**
 * Created by radu on 3/28/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatInfo {
  private int rnk = -1;
  private int id = -1;
  private String name = "";
  private int win = 0;
  private int drw = 0;
  private int los = 0;
  private int gsc = 0;
  private int gre = 0;
  private int pts = 0;
  private List<Integer> pen = Arrays.asList(0);
  private List<Integer> pdt = Arrays.asList(0);
  private String dec = "";

  public static StatInfo from(Stat s) {
    return new StatInfo(-1, s.getId(), "",
        s.getWin(), s.getDrw(), s.getLos(), s.getGsc(), s.getGre(),
        0, Arrays.asList(s.getPen()), Arrays.asList(s.getPdt()), "");
  }
}
