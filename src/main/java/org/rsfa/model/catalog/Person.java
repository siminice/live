package org.rsfa.model.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by radu on 12/13/16.
 */
@Data
@AllArgsConstructor
public class Person {
  String mnem = "";
  String lastName = "";
  String firstName = "";
  String displayName = "";
  String nationality = "";
  String birthDate = "";
  String birthPlace = "";
  String birthCountry = "";
  String deceased = "";

  public String serialize() {
    return String.join(",", mnem, lastName, firstName, displayName, birthDate, nationality, birthPlace, birthCountry, deceased);
  }

  public String serializeShort() {
    return String.join(",", mnem, lastName, firstName, birthDate, nationality, birthPlace, birthCountry);
  }

  public String initial() {
    if (firstName.length()>0 && firstName.charAt(0)!=' ') return firstName.substring(0,1) + ". ";
    else return "";
  }

  @Override
  public String toString() {
    return initial() + lastName;
  }
}
