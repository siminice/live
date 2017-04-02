package org.rsfa.service;

import lombok.extern.slf4j.Slf4j;
import org.rsfa.internal.ResultInfo;
import org.rsfa.internal.ScoreInfo;
import org.rsfa.internal.SeasonInfo;
import org.rsfa.model.league.Fed;
import org.rsfa.model.league.League;
import org.rsfa.model.league.LeagueMetadata;
import org.rsfa.model.league.LeaguePrinter;
import org.rsfa.model.results.FixtureResult;
import org.rsfa.model.stats.StatSorter;
import org.rsfa.util.Constants;
import org.rsfa.util.Syntax;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by radu on 3/28/17.
 */
@Slf4j
public class InMemoryLeagueService implements LeagueService {

  private Map<String, Fed> feds = new HashMap<>();
  private Map<String, League> leagues = new HashMap<>();

  @Override
  public SeasonInfo getSeason(final String ctty,
                                  final String tier,
                                  final String season) {
    League lg = getLeague(ctty + "/" + tier + "/" + season);
    lg.setMetadata(new LeagueMetadata(Integer.parseInt(season), tier, 0));
    lg.sort();
    return SeasonInfo.from(lg);
  }

  @Override
  public List<ResultInfo> getResults(final String ctty,
                                     final String tier,
                                     final String season,
                                     final int home,
                                     final int away) {
    League lg = getLeague(ctty + "/" + tier + "/" + season);
    if (lg == null) {
      return Collections.EMPTY_LIST;
    };
    lg.sort();
    System.out.println(lg.toString());
    int h = lg.findId(home);
    int a = lg.findId(away);
    if (h == Constants.UNKNOWN || a == Constants.UNKNOWN) return Collections.EMPTY_LIST;
    List<FixtureResult> res = lg.getRes().vs(h, a);
    return res.stream()
        .map(r -> ResultInfo.from(r, lg.nickOf(h), lg.nickOf(a)))
        .collect(Collectors.toList());
  }

  @Override
  public ResultInfo setResult(final String ctty,
                              final String tier,
                              final String season,
                              final int home,
                              final int away,
                              final ScoreInfo si) {
    League lg = getLeague(ctty + "/" + tier + "/" + season);
    if (lg == null) {
      return null;
    };
    int h = lg.findId(home);
    int a = lg.findId(away);
    int y = Integer.parseInt(season);
    if (h == Constants.UNKNOWN || a == Constants.UNKNOWN) return null;
    FixtureResult fr = FixtureResult.from(h, a, y, si.getRound(), si.getScore());
    lg.addResult(fr);
    lg.countResult(fr);
    StatSorter ss = new StatSorter(lg);
    ss.sort();
    LeaguePrinter lp = new LeaguePrinter(lg, Arrays.asList(h, a));
    System.out.println(lp.print());
    return ResultInfo.from(fr, lg.nickOf(h), lg.nickOf(a));
  }

  @Override
  public List<ResultInfo> getRound(final String ctty,
                                   final String tier,
                                   final String season,
                                   final int rd) {
    League lg = getLeague(ctty + "/" + tier + "/" + season);
    if (lg == null) {
      return Collections.EMPTY_LIST;
    };
    StatSorter ss = new StatSorter(lg);
    ss.sort();
    System.out.println(lg.toString());
    Collection<FixtureResult> res = lg.getRes().round(rd);
    return res.stream()
        .map(r -> ResultInfo.from(r,
            lg.nickOf(r.getFixture().getHome()),
            lg.nickOf(r.getFixture().getAway())))
        .collect(Collectors.toList());
  }

  private League getLeague(final String lgn) {
    League lg = leagues.get(lgn);
    if (lg==null) lg = loadLeague(lgn);
    if (lg!=null) leagues.put(lgn, lg);
    return lg;
  }

  private League loadLeague(final String lgn) {
    String[] tk = lgn.split("/");
    if (tk.length < 3) return null;
    Fed fed = getFed(tk[0]);
    League lg = new League(fed);
    lg.load(repo + tk[0] + "/" + tk[1] + "." + tk[2]);
    lg.countAllResults();
    return lg;
  }

  private Fed getFed(final String ctty) {
    Fed fed = feds.get(ctty);
    if (fed==null) fed = loadFed(ctty);
    if (fed!=null) feds.put(ctty, fed);
    return fed;
  }

  private Fed loadFed(final String ctty) {
    String path = repo + ctty;
    Fed fed = new Fed(ctty);
    fed.loadClubs(path + "/teams.dat", Syntax.FIXED);
    fed.loadChainedAliases(path + "/alias.dat");
    return fed;
  }

  @Value("${repo}")
  private String repo;
}
