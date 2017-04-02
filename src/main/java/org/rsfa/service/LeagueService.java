package org.rsfa.service;

import org.rsfa.internal.ResultInfo;
import org.rsfa.internal.ScoreInfo;
import org.rsfa.internal.SeasonInfo;

import java.util.List;

/**
 * Created by radu on 3/28/17.
 */
public interface LeagueService {
  public SeasonInfo getSeason(final String ctty,
                              final String tier,
                              final String season);

  public List<ResultInfo> getResults(final String ctty,
                                     final String tier,
                                     final String season,
                                     final int home,
                                     final int away);

  public ResultInfo setResult(final String ctty,
                              final String tier,
                              final String season,
                              final int home,
                              final int away,
                              final ScoreInfo sc);

  public List<ResultInfo> getRound(final String ctty,
                                   final String tier,
                                   final String season,
                                   final int round);
}
