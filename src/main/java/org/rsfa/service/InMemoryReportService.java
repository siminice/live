package org.rsfa.service;

import org.rsfa.model.catalog.MatchReport;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by radu on 4/25/17.
 */
public class InMemoryReportService implements ReportService {
  private Map<String, Collection<MatchReport>> reports = new HashMap();

  @Override
  public MatchReport getReport(String lgn, String home, String away) {
    Collection<MatchReport> allRep = getLeague(lgn, false);
    return allRep.stream()
        .filter(r -> r.getHome().equals(home) && r.getAway().equals(away))
        .findFirst().orElse(null);
  }

  @Override
  public MatchReport getReport(String lgn, String home, String away, String date) {
    Collection<MatchReport> allRep = getLeague(lgn, false);
    return allRep.stream()
        .filter(r -> r.getHome().equals(home) && r.getAway().equals(away) && r.getDate().equals(date))
        .findFirst().orElse(null);
  }

  /*
   * Helper methods
   */

  private Collection<MatchReport> getLeague(final String lgn, final Boolean reload) {
    Collection<MatchReport> lg = null;
    if (!reload) lg = reports.get(lgn);
    if (lg==null) lg = loadLeague(lgn);
    if (lg!=null) reports.put(lgn, lg);
    return lg;
  }

  private Collection<MatchReport> loadLeague(final String lgn) {
    String[] tk = lgn.split("/");
    if (tk.length < 3) return null;
    Collection<MatchReport> lg = new ArrayList<MatchReport>();
    String filename = location + "lineups-" + tk[2] + ".db";
    try {
      final FileInputStream fstream = new FileInputStream(filename);
      final InputStreamReader dis = new InputStreamReader(fstream, "ISO-8859-2");
      final BufferedReader br = new BufferedReader(dis);
      String s;
      while ((s = br.readLine()) != null) {
          lg.add(MatchReport.from(s));
      }
      br.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }
    return lg;
  }

  @Value("${reports}")
  private String location;
}
