package org.rsfa.service;

import org.rsfa.model.catalog.MatchEvent;
import org.rsfa.model.catalog.MatchReport;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by radu on 4/25/17.
 */
public class InMemoryReportService implements ReportService {
  private Map<String, List<MatchReport>> reports = new HashMap();
  private Map<String, List<MatchEvent>> events = new HashMap();

  @Override
  public MatchReport getReport(String lgn, String home, String away) {
    List<MatchReport> allRep = getLienups(lgn, false);
    MatchReport report =  allRep.stream()
        .filter(r -> r.getHome().equals(home) && r.getAway().equals(away))
        .findFirst().orElse(null);
    return report;
  }

  @Override
  public MatchReport getReport(String lgn, String home, String away, String date) {
    List<MatchReport> allRep = getLienups(lgn, false);
    MatchReport report =  allRep.stream()
        .filter(r -> r.getHome().equals(home) && r.getAway().equals(away) && r.getDate().equals(date))
        .findFirst().orElse(null);
    return report;
  }

  /*
   * Helper methods
   */

  private List<MatchReport> getLienups(final String lgn, final Boolean reload) {
    List<MatchReport> lg = null;
    if (!reload) lg = reports.get(lgn);
    if (lg==null) lg = loadLineups(lgn);
    if (lg!=null) reports.put(lgn, lg);
    return lg;
  }

  private List<MatchReport> loadLineups(final String lgn) {
    String[] tk = lgn.split("/");
    if (tk.length < 3) return null;
    List<MatchReport> reports = new ArrayList<MatchReport>();
    String filename = location + "db/lineups-" + tk[2] + ".db";
    try {
      final FileInputStream fstream = new FileInputStream(filename);
      final InputStreamReader dis = new InputStreamReader(fstream, "ISO-8859-2");
      final BufferedReader br = new BufferedReader(dis);
      String s;
      while ((s = br.readLine()) != null) {
          reports.add(MatchReport.from(s));
      }
      br.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }
    attachEvents(reports, lgn);
    return reports;
  }

  private void attachEvents(final List<MatchReport> reps, final String lgn) {
    String[] tk = lgn.split("/");
    if (tk.length < 3) return;
    String filename = location + "db/events-" + tk[2] + ".db";
    try {
      final FileInputStream fstream = new FileInputStream(filename);
      final InputStreamReader dis = new InputStreamReader(fstream, "ISO-8859-2");
      final BufferedReader br = new BufferedReader(dis);
      String s;
      for (MatchReport r : reps) {
        s = br.readLine();
        r.setEvents(MatchEvent.from(s));
      }
      br.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  @Value("${reports}")
  private String location;
}
