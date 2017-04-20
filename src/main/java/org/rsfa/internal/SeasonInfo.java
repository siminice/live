package org.rsfa.internal;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.rsfa.model.league.Fed;
import org.rsfa.model.league.League;
import org.rsfa.model.results.FixtureResult;
import org.rsfa.model.stats.Stat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by radu on 3/28/17.
 */
@Data
@NoArgsConstructor
public class SeasonInfo {
  private LeagueInfo leagueInfo;
  private LeagueFormatInfo formatInfo;
  private List<Integer> ids;
  private List<Integer> rank;
  private List<StatInfo> stats;
  private List<ResultInfo> results;

  public static SeasonInfo from(League l, int round1, int round2) {
    SeasonInfo si = new SeasonInfo();
    if (l==null) return si;
    Fed fed = l.getFed();
    LeagueInfo li = new LeagueInfo();
    li.setCountry(fed.getCtty());
    si.setLeagueInfo(li);
    si.setFormatInfo(LeagueFormatInfo.from(l.getFormat()));
    si.setIds(new ArrayList<Integer>(Arrays.asList(l.getId())));
    l.resetStats();

    Collection<FixtureResult> partial = l.getRes().roundFilter(r -> r>=round1 && r<=round2);

    si.setResults(partial.stream()
        .map(r -> ResultInfo.from(r,
            l.nickOf(r.getFixture().getHome()),
            l.nickOf(r.getFixture().getAway())))
        .collect(Collectors.toList()));

    partial.stream().forEach(r -> l.countResult(r));
    ArrayList<Stat> statList = new ArrayList<Stat>(Arrays.asList(l.getStat()));
    List<StatInfo> stats = statList.stream()
        .map(s -> StatInfo.from(s))
        .collect(Collectors.toList());
    stats.stream().forEach(s -> {
      int idx = l.findId(s.getId());
      s.setName(fed.nameOf(s.getId(), l.getMetadata().getYear()));
      s.setPts(l.getFormat().getPpv() * s.getWin() + s.getDrw());
      s.setRnk(l.rankOf(idx) + 1);
      s.setDec(l.getDeco()[idx]);
    });
    si.setStats(stats);
    l.sort();
    si.setRank(new ArrayList<Integer>(Arrays.asList(l.getRank())));

    return si;
  }
}
