package org.rsfa.model.catalog;

import lombok.Data;

/**
 * Created by radu on 12/13/16.
 */
@Data
public class Venue {
  String mnem;
  String name;
  int capacity;
  int built;
  int city;
}
