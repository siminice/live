package org.rsfa.model.results;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.rsfa.model.stats.Stat;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by radu on 12/1/16.
 */
@Slf4j
public class Results {
  @Setter @Getter private int n;
  @Setter @Getter private int m;
  @Setter private Collection<FixtureResult> data;

  public Results(int n, int m) {
    this.n = n;
    this.m = m;
    data = new ArrayList<FixtureResult>();
  }

  public Collection<FixtureResult> getAll() {
    return data;
  }

  public List<FixtureResult> vs(int i, int j) {
    return data.stream().filter(r ->
        r.getFixture().getHome() == i && r.getFixture().getAway() == j)
        .collect(Collectors.toList());
  }

  public FixtureResult getResult(int i, int j, int k) {
    List<FixtureResult> resij = vs(i, j);
    if (resij.isEmpty() || k<0 || k>=resij.size()) return new FixtureResult();
    return resij.get(k);
  }

  public void add(FixtureResult r) {
    data.add(r);
  }

  public void delete(FixtureResult r) { data.remove(r); }

  public Stat vsStat(int i, int j) {
    Stat s = new Stat();
    vs(i, j).forEach(r -> s.addResult(r.getResult()));
    return s;
  }

  public Collection<FixtureResult> round(int r) {
    return data.stream()
        .filter(x -> x.getFixture().getRound() == r)
        .sorted(FixtureResult.byDate)
        .collect(Collectors.toList());
  }

  public Collection<FixtureResult> date(LocalDate z) {
    return data.stream()
        .filter(x -> x.getResult().getZ().isEqual(z))
        .collect(Collectors.toList());
  }


  public Collection<FixtureResult> filterByTeam(int t) {
    return data.stream()
        .filter(r -> r.getFixture().has(t))
        .collect(Collectors.toList());
  }


  public Map<Integer, Long> roundsFreq() {
    return data.stream()
        .map(r -> r.getFixture().getRound())
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }

  public Map<Integer, Long> roundsFreqByTeam(int t) {
    return data.stream()
        .filter(r -> r.getFixture().has(t))
        .map(r -> r.getFixture().getRound())
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }

  public Set<Integer> availableRounds(int t) {
    Set<Integer> allRounds = data.stream()
        .map(r -> r.getFixture().getRound())
        .filter(r -> r>0)
        .collect(Collectors.toSet());
    allRounds.removeAll(roundsFreqByTeam(t).keySet());
    return allRounds;
  }

  public Set<Integer> availableRounds(int h, int a) {
    Set<Integer> avh = availableRounds(h);
    Set<Integer> ava = availableRounds(a);
    avh.retainAll(ava);
    return avh;
  }

  public Integer firstRound() {
    return data.stream()
        .mapToInt(r -> r.getFixture().getRound())
        .filter(r -> r > 0)
        .min().orElse(-1);
  }

  public Integer lastRound() {
    return data.stream()
        .mapToInt(r -> r.getFixture().getRound())
        .max().orElse(-1);
  }

  public Set<LocalDate> datesOfRound(int r) {
    Collection<FixtureResult> first = round(1);
    return first.stream()
        .map(fr -> fr.getResult().getZ())
        .collect(Collectors.toSet());
  }

  public LocalDate firstDate() {
    return datesOfRound(1).stream()
        .min((d1, d2) -> d1.compareTo(d2)).orElse(LocalDate.MAX);
  }

  public LocalDate lastDate() {
    return data.stream()
        .map(d -> d.getResult().getZ())
        .max((d1, d2) -> d1.compareTo(d2)).orElse(LocalDate.MIN);
  }

  public void adjustYear() {
    LocalDate first = firstDate();
    data.stream()
        .forEach(r -> {
          TimedResult curr = r.getResult();
          LocalDate curd = curr.getZ();
          if (!curd.isBefore(first)) {
            curr.setZ(curd.minusYears(1));
          }
        });
    Map<Integer, List<FixtureResult>> groups = data.stream()
        .collect(Collectors.groupingBy(r->r.getResult().getZ().getYear()));
    groups.keySet().stream()
        .forEach(k -> log.error("{} -> {}", k, groups.get(k).size()));
  }

  public String serialize(int i, int k) {
    StringBuilder sb = new StringBuilder();
    for (int j=0; j<n; j++) {
      sb.append(getResult(i, j, k).serialize()+" ");
    }
    return sb.toString();
  }

  public String serialize() {
    StringBuilder sb = new StringBuilder();
    for (int k=0; k<m; k++) {
      for (int i=0; i<n; i++) {
        sb.append(serialize(i, k)+"\n");
      }
    }
    return sb.toString();
  }
}
