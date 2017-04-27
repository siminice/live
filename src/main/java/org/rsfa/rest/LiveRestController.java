package org.rsfa.rest;

import lombok.extern.slf4j.Slf4j;
import org.rsfa.internal.FedInfo;
import org.rsfa.internal.ResultInfo;
import org.rsfa.internal.ScoreInfo;
import org.rsfa.internal.SeasonInfo;
import org.rsfa.model.catalog.Catalog;
import org.rsfa.model.catalog.Locations;
import org.rsfa.model.catalog.MatchReport;
import org.rsfa.model.catalog.Person;
import org.rsfa.service.FedService;
import org.rsfa.service.LeagueService;
import org.rsfa.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by radu on 3/27/17.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class LiveRestController {
  private final int MAX_ROUND = 99999;

  @Autowired
  FedService fedService;
  @Autowired
  LeagueService leagueService;
  @Autowired
  ReportService reportService;

  @RequestMapping(value="/leagues", method = RequestMethod.GET)
  public ResponseEntity<List<FedInfo>> listAllFeds() {
    List<FedInfo> allFeds = fedService.getAllFeds();
    if(allFeds.isEmpty()){
      return new ResponseEntity<List<FedInfo>>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<List<FedInfo>>(allFeds, HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}", method = RequestMethod.GET)
  public ResponseEntity<SeasonInfo> getLeague(
      @PathVariable("ctty") String ctty,
      @PathVariable("tier") String tier,
      @PathVariable("season") String season,
      @RequestParam(value="reload", required=false) Boolean reload) {
    try {
      SeasonInfo si = leagueService.getSeason(ctty, tier, season, 0, MAX_ROUND, reload!=null && reload.booleanValue());
      return new ResponseEntity<SeasonInfo>(si, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<SeasonInfo>(new SeasonInfo(), HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}", method = RequestMethod.PUT)
  public ResponseEntity<Boolean> saveLeague(
      @PathVariable("ctty") String ctty,
      @PathVariable("tier") String tier,
      @PathVariable("season") String season) {
    return new ResponseEntity<Boolean>(leagueService.saveSeason(ctty, tier, season), HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}/results/{home}/vs/{away}", method = RequestMethod.GET)
  public ResponseEntity<List<ResultInfo>> getResults(
      @PathVariable("ctty") String ctty,
      @PathVariable("tier") String tier,
      @PathVariable("season") String season,
      @PathVariable("home") String home,
      @PathVariable("away") String away) {
    List<ResultInfo> allRes = leagueService.getResults(ctty, tier, season, home, away);
    return new ResponseEntity<List<ResultInfo>>(allRes, HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}/results/{home}/all/{away}", method = RequestMethod.GET)
  public ResponseEntity<List<ResultInfo>> getBothResults(
      @PathVariable("ctty") String ctty,
      @PathVariable("tier") String tier,
      @PathVariable("season") String season,
      @PathVariable("home") String home,
      @PathVariable("away") String away) {
    List<ResultInfo> resHome = leagueService.getResults(ctty, tier, season, home, away);
    List<ResultInfo> resAway = leagueService.getResults(ctty, tier, season, away, home);
    resHome.addAll(resAway);
    return new ResponseEntity<List<ResultInfo>>(resHome, HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}/rounds/{home}/vs/{away}", method = RequestMethod.GET)
  public ResponseEntity<List<Integer>> getAvailableRounds(
      @PathVariable("ctty") String ctty,
      @PathVariable("season") String season,
      @PathVariable("tier") String tier,
      @PathVariable("home") String home,
      @PathVariable("away") String away) {
    List<Integer> common = leagueService.getAvailableRounds(ctty, tier, season, home, away);
    return new ResponseEntity<List<Integer>>(common, HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}/reports/{home}/vs/{away}", method = RequestMethod.GET)
  public ResponseEntity<MatchReport> getReport(
      @PathVariable("ctty") String ctty,
      @PathVariable("tier") String tier,
      @PathVariable("season") String season,
      @PathVariable("home") String home,
      @PathVariable("away") String away) {
    MatchReport report = reportService.getReport(ctty + "/" + tier + "/" + season, home, away);
    mapNames(report);
    return new ResponseEntity<MatchReport>(report, HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}/results/{home}/vs/{away}",
      method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8")
  public ResponseEntity<ResultInfo> setResult(
      @PathVariable("ctty") String ctty,
      @PathVariable("tier") String tier,
      @PathVariable("season") String season,
      @PathVariable("home") String home,
      @PathVariable("away") String away,
      @RequestBody ScoreInfo sc) {
    ResultInfo result = leagueService.setResult(ctty, tier, season, home, away, sc);
    return new ResponseEntity<ResultInfo>(result, HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}/results/{home}/vs/{away}/{round}",
      method = RequestMethod.DELETE)
  public ResponseEntity<ResultInfo> deleteResult(
      @PathVariable("ctty") String ctty,
      @PathVariable("tier") String tier,
      @PathVariable("season") String season,
      @PathVariable("home") String home,
      @PathVariable("away") String away,
      @PathVariable("round") int round) {
    ResultInfo result = leagueService.deleteResult(ctty, tier, season, home, away, round);
    return new ResponseEntity<ResultInfo>(result, HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}/rounds", method = RequestMethod.GET)
  public ResponseEntity<List<Integer>> getRound(
      @PathVariable("ctty") String ctty,
      @PathVariable("tier") String tier,
      @PathVariable("season") String season) {
    List<Integer> allRes = leagueService.getAllRounds(ctty, tier, season);
    return new ResponseEntity<List<Integer>>(allRes, HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}/rounds/{round}", method = RequestMethod.GET)
  public ResponseEntity<List<ResultInfo>> getRound(
      @PathVariable("ctty") String ctty,
      @PathVariable("tier") String tier,
      @PathVariable("season") String season,
      @PathVariable("round") int round) {
    List<ResultInfo> allRes = leagueService.getRound(ctty, tier, season, round);
    return new ResponseEntity<List<ResultInfo>>(allRes, HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}/stats/from/{round1}/to/{round2}", method = RequestMethod.GET)
  public ResponseEntity<SeasonInfo> getStats(
      @PathVariable("ctty") String ctty,
      @PathVariable("tier") String tier,
      @PathVariable("season") String season,
      @PathVariable("round1") int round1,
      @PathVariable("round2") int round2) {
    try {
      SeasonInfo si = leagueService.getSeason(ctty, tier, season, round1, round2, false);
      return new ResponseEntity<SeasonInfo>(si, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<SeasonInfo>(new SeasonInfo(), HttpStatus.NOT_FOUND);
    }

  }

  @Value("${reports}") private String path;

  private Catalog players = null;
  private Catalog coaches = null;
  private Catalog referees = null;
  private Locations stadiums = null;

  private Catalog retrieve(Catalog c, String filename) {
    if (c==null) {
      c = new Catalog();
      c.load(path + "catalogs/" + filename + ".dat");
    }
    return c;
  }

  private Locations retrieve(Locations loc, String filename) {
    if (loc==null) {
      loc = new Locations();
      loc.load(path + "catalogs/" + filename + ".dat");
    }
    return loc;
  }

  private void mapNames(MatchReport report) {
    players = retrieve(players, "players");
    coaches = retrieve(coaches, "coaches");
    referees = retrieve(referees, "referees");
    report.getHroster().stream().forEach(c-> {
      Optional<Person> p = players.findMnem(c.getName());
      if (p.isPresent()) c.setName(p.get().toString());
    });
    report.getAroster().stream().forEach(c-> {
      Optional<Person> p = players.findMnem(c.getName());
      if (p.isPresent()) c.setName(p.get().toString());
    });
    report.getEvents().stream().forEach(c-> {
      Optional<Person> p = players.findMnem(c.getName());
      if (p.isPresent()) c.setName(p.get().longName());
    });
    Optional<Person> p;
    p = coaches.findMnem(report.getHcoach());
    if (p.isPresent()) report.setHcoach(p.get().longName());
    p = coaches.findMnem(report.getAcoach());
    if (p.isPresent()) report.setAcoach(p.get().longName());
    p = referees.findMnem(report.getRef());
    if (p.isPresent()) report.setRef(p.get().longName());
  }

}