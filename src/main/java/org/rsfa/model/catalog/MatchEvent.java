package org.rsfa.model.catalog;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by radu on 4/25/17.
 */
@Data
public class MatchEvent {
  String mnem = "";
  String type = "";
  String min = "";

  public static MatchEvent parse(String line) {
    MatchEvent ev = new MatchEvent();
    if (line != null && line.length() >= 10) {
      ev.setMnem(line.substring(0,6).trim());
      ev.setType(line.substring(6,6));
      ev.setMin(line.substring(7,10).trim());
    }
    return ev;
  }

  public static List<MatchEvent> from(String line) {
    List<MatchEvent> list = new ArrayList();
    if (line != null) {
      for (String tk : line.split(",")) {
        MatchEvent ev = parse(tk);
        if (ev.getMnem().length()>0) list.add(ev);
      }
    }
    return list;
  }
}
