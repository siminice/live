package org.rsfa.model.catalog;

/**
 * Created by radu on 4/25/17.
 */

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Created by radu on 12/13/16.
 */
public class Catalog {
  @Getter
  private Collection<Person> cat;
  private Map<Character, Integer> borna;

  public int size() {
    return cat.size();
  }

  public void load(String filename) {
    cat = new ArrayList<Person>();
    try {
      final FileInputStream fstream = new FileInputStream(filename);
      final InputStreamReader dis = new InputStreamReader(fstream, "ISO-8859-2");
      final BufferedReader br = new BufferedReader(dis);
      String s = br.readLine();
      while ((s = br.readLine()) != null) {
        final String[] tk = s.split(",");
        if (tk.length > 6) {
          Person p = new Person(tk[1], tk[0], tk[2], " ", tk[3], tk[4], tk[5], tk[6], " ");
          p.setDisplayName(p.initial() + p.getLastName());
          cat.add(p);
          System.out.println("Loaded: " + p.serialize());
        }
      }
      br.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public boolean save(String filename) {
    try {
      final FileOutputStream fstream = new FileOutputStream(filename);
      final OutputStreamWriter dis = new OutputStreamWriter(fstream, "ISO-8859-2");
      final BufferedWriter bw = new BufferedWriter(dis);
      bw.write(String.format("%d\n", cat.size()));
      for (Person p : cat) {
        bw.write(p.serializeShort());
        bw.newLine();
      }
      bw.close();
      return true;
    } catch (final Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public Optional<Person> findMnem(String s) {
    return cat.stream()
        .filter(p->p.getMnem().equals(s))
        .findAny();
  }

  public int binFindMnem(String s) {
    return -1;
  }

  public int add(Person p) {
    cat.add(p);
    return cat.size();
  }

  public int addSuggest(Person p) {
    return cat.size();
  }

  public String getInitial(int i) {
    return ".";
  }

}
