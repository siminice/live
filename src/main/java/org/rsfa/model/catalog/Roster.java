package org.rsfa.model.catalog;

import lombok.Data;

/**
 * Created by radu on 12/21/16.
 */
@Data
public class Roster {
  private Player player;
  private Person person;
  private PlayerStat stat;

  public String toString() {
    return "";
  }
}

