package org.rsfa.model.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

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
  private String[] hroster = new String[ROSTER_SIZE];
  private String hcoach = "";
  private String[] aroster = new String[ROSTER_SIZE];
  private String acoach = "";
  private String ref = "";
  private String assist1 = "";
  private String assist2 = "";
  private String observ = "";
  private List<MatchEvent> events = Collections.EMPTY_LIST;

  public String serialize() {
    return String.join(",", home, away, result, date, league, round, venue, attendance, weather,
        String.join(",", hroster), hcoach,
        String.join(",", aroster), acoach,
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
      r.setHroster(new String[]{
          tok[9], tok[10], tok[11], tok[12], tok[13], tok[14], tok[15], tok[16], tok[17], tok[18], tok[19],
          tok[20], tok[21], tok[22], tok[23], tok[24], tok[25], tok[26], tok[27], tok[28], tok[29], tok[30]
      });
      r.setHcoach(tok[31]);
      r.setAroster(new String[]{
          tok[32], tok[33], tok[34], tok[35], tok[36], tok[37], tok[38], tok[39], tok[40], tok[41], tok[42],
          tok[43], tok[44], tok[45], tok[46], tok[47], tok[48], tok[49], tok[50], tok[51], tok[52], tok[53]
      });
      r.setAcoach(tok[54]);
      r.setRef(tok[55]);
      r.setAssist1(tok[56]);
      r.setAssist2(tok[57]);
      r.setObserv(tok[58]);
    }
    return r;
  }
}
