package org.rsfa.model.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
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

  private String home = "";
  private String away = "";
  private String result = "";
  private String date = "";
  private String league = "";
  private String round = "";
  private String venue = "";
  private String attendance ="";
  private String weather = "";
  private Cap[] hroster = new Cap[0];
  private String hcoach = "";
  private Cap[] aroster = new Cap[0];
  private String acoach = "";
  private String ref = "";
  private String assist1 = "";
  private String assist2 = "";
  private String observ = "";
  private List<MatchEvent> events = Collections.EMPTY_LIST;

  public String lineup() {
    return String.join(",", home, away, result, date, league, round, venue, attendance, weather,
        Arrays.stream(hroster).map(Cap::toString).collect(Collectors.joining(",")), hcoach,
        Arrays.stream(aroster).map(Cap::toString).collect(Collectors.joining(",")), acoach,
        ref, assist1, assist2, observ);
  }

  public static MatchReport from(String line) {
    String[] tok = line.split(",");
    MatchReport r = new MatchReport();
    if (tok.length >= 60) {
      r.setHome(tok[0].trim());
      r.setAway(tok[1].trim());
      r.setResult(tok[2].trim());
      r.setDate(tok[3].trim());
      r.setLeague(tok[4].trim());
      r.setRound(tok[5].trim());
      r.setVenue(tok[6].trim());
      r.setAttendance(tok[7].trim());
      r.setWeather(tok[8].trim());
      Cap[] hr = new Cap[ROSTER_SIZE];
      for (int i=0; i<ROSTER_SIZE; i++) hr[i] = Cap.from(tok[9+i]);
      r.setHroster(hr);
      r.setHcoach(tok[31].trim());
      Cap[] ar = new Cap[ROSTER_SIZE];
      for (int i=0; i<ROSTER_SIZE; i++) ar[i] = Cap.from(tok[32+i]);
      r.setAroster(ar);
      r.setAcoach(tok[54].trim());
      r.setRef(tok[55].trim());
      r.setAssist1(tok[56].trim());
      r.setAssist2(tok[57].trim());
      r.setObserv(tok[58].trim());
    }
    return r;
  }

  public void mapNames(Catalog cat) {
    Arrays.stream(hroster).forEach(c-> {
      Optional<Person> p = cat.findMnem(c.getName());
      if (p.isPresent()) c.setName(p.get().toString());
    });
    Arrays.stream(aroster).forEach(c-> {
      Optional<Person> p = cat.findMnem(c.getName());
      if (p.isPresent()) c.setName(p.get().toString());
    });
    events.stream().forEach(c-> {
      Optional<Person> p = cat.findMnem(c.getName());
      if (p.isPresent()) c.setName(p.get().toString());
    });
  }
}
