package org.rsfa.model.league;

import lombok.Data;

/**
 * Created by radu on 3/27/17.
 */
@Data
public class LeagueFormat {
  private Integer size;
  private Integer ppv = 2;
  private Integer[] promo;
  private Integer[] releg;
  private Integer groups = 1;
  private Integer tbr;
  private Integer rounds = 1;
  private Boolean winter = false;

  @Override
  public String toString() {
    return String.format("%d %d %d %d %d %d %d",
        size, ppv, tbr, promo[0], promo[1], releg[0], releg[1]);
  }
}
