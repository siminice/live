package org.rsfa.model.catalog;

import lombok.Data;

import java.util.Collection;

/**
 * Created by radu on 12/13/16.
 */
@Data
public class Locations {
  Collection<City> city;
  Collection<Venue> venue;

  public void load(String filename) {

  }

  public void save(String filename) {

  }

  public int findCity(String s) {
    return -1;
  }

  public int findVenue(String s) {
    return -1;
  }
}

