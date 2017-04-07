package org.rsfa.service;

import org.rsfa.internal.ResultInfo;
import org.rsfa.internal.ScoreInfo;
import org.rsfa.internal.SeasonInfo;

import java.util.List;

/**
 * Created by radu on 3/28/17.
 */
public interface LeagueService {
  public SeasonInfo getSeason(
      final String ctty,
      final String tier,
      final String season,
      final boolean reload);

  public Boolean saveSeason(
      final String ctty,
      final String tier,
      final String season);

  public List<ResultInfo> getResults(
      final String ctty,
      final String tier,
      final String season,
      final String home,
      final String away);

  public List<Integer> getAvailableRounds(
      final String ctty,
      final String tier,
      final String season,
      final String home,
      final String away);

  public ResultInfo setResult(
      final String ctty,
      final String tier,
      final String season,
      final String home,
      final String away,
      final ScoreInfo sc);

  public ResultInfo deleteResult(
      final String ctty,
      final String tier,
      final String season,
      final String home,
      final String away,
      final int round);

  public List<ResultInfo> getRound(
      final String ctty,
      final String tier,
      final String season,
      final int round);
}
