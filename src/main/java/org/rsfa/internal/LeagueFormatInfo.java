package org.rsfa.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.rsfa.model.league.LeagueFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by radu on 4/2/17.
 */
@Data
@AllArgsConstructor
public class LeagueFormatInfo {
  private Integer size;
  private Integer ppv = 2;
  private List<Integer> promo;
  private List<Integer> releg;
  private Integer groups = 1;
  private Integer tbr;
  private Integer rounds = 1;
  private Boolean winter = false;

  public static LeagueFormatInfo from(final LeagueFormat f) {
    return new LeagueFormatInfo(
        f.getSize(), f.getPpv(),
        new ArrayList<Integer>(Arrays.asList(f.getPromo())),
        new ArrayList<Integer>(Arrays.asList(f.getReleg())),
        f.getGroups(), f.getTbr(), f.getRounds(), f.getWinter()
        );
  }
}
