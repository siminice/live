package org.rsfa.model.catalog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by radu on 4/25/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchReport {
  public static final int ROSTER_SIZE = 22;
  public static final int NUM_REPORT_FIELDS = 59;

  private String home = "";
  private String away = "";
  private String result = "";
  private String date = "";
  private String league = "";
  private String round = "";
  private String venue = "";
  private String attendance ="";
  private String weather = "";
  private List<Cap> hroster = Collections.EMPTY_LIST;
  private String hcoach = "";
  private List<Cap> aroster = Collections.EMPTY_LIST;
  private String acoach = "";
  private String ref = "";
  @JsonIgnore
  private String assist1 = "";
  @JsonIgnore
  private String assist2 = "";
  @JsonIgnore
  private String observ = "";
  private List<MatchEvent> events = Collections.EMPTY_LIST;

  public String lineup() {
    return String.join(",", home, away, result, date, league, round, venue, attendance, weather,
        hroster.stream().map(Cap::toString).collect(Collectors.joining(",")), hcoach,
        aroster.stream().map(Cap::toString).collect(Collectors.joining(",")), acoach,
        ref, assist1, assist2, observ);
  }

  public static MatchReport from(String line) {
    String[] tok = line.split(",");
    MatchReport r = new MatchReport();
    if (tok.length >= NUM_REPORT_FIELDS) {
      r.setHome(tok[0].trim());
      r.setAway(tok[1].trim());
      int res = Integer.parseInt(tok[2].trim());
      r.setResult(res>=0? String.format("%d-%d", res/100, res%100) : "?");
      r.setDate(tok[3].trim());
      r.setLeague(tok[4].trim());
      r.setRound(tok[5].trim());
      r.setVenue(tok[6].trim());
      r.setAttendance(tok[7].trim());
      r.setWeather(tok[8].trim());
      List<Cap> hr = new ArrayList();
      for (int i=0; i<ROSTER_SIZE; i++) {
        Cap c = Cap.from(tok[9+i]); if (!c.equals(Cap.UNKNOWN)) hr.add(c);
      }
      r.setHroster(hr);
      r.setHcoach(tok[31].trim());
      List<Cap> ar = new ArrayList();
      for (int i=0; i<ROSTER_SIZE; i++) {
        Cap c = Cap.from(tok[32+i]); if (!c.equals(Cap.UNKNOWN)) ar.add(c);
      }
      r.setAroster(ar);
      r.setAcoach(tok[54].trim());
      r.setRef(tok[55].trim());
      r.setAssist1(null);
      r.setAssist2(null);
      r.setObserv(null);
    }
    return r;
  }

  public void mapNames(Catalog cat) {
    hroster.stream().forEach(c-> {
      Optional<Person> p = cat.findMnem(c.getName());
      if (p.isPresent()) c.setName(p.get().toString());
    });
    aroster.stream().forEach(c-> {
      Optional<Person> p = cat.findMnem(c.getName());
      if (p.isPresent()) c.setName(p.get().toString());
    });
    events.stream().forEach(c-> {
      Optional<Person> p = cat.findMnem(c.getName());
      if (p.isPresent()) c.setName(p.get().toString());
    });
  }
}
