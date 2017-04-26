package org.rsfa.service;

import org.rsfa.model.catalog.MatchReport;

/**
 * Created by radu on 4/25/17.
 */
public interface ReportService {
  public MatchReport getReport(String lgn, String home, String away);

  public MatchReport getReport(String lgn, String home, String away, String date);
}
