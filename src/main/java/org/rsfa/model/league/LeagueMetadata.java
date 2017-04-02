package org.rsfa.model.league;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by radu on 3/27/17.
 */
@Data
@AllArgsConstructor
public class LeagueMetadata {
  private int year;
  private String tier;
  private int pool;
}
