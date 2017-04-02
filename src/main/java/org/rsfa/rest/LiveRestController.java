package org.rsfa.rest;

import lombok.extern.slf4j.Slf4j;
import org.rsfa.internal.FedInfo;
import org.rsfa.internal.ResultInfo;
import org.rsfa.internal.ScoreInfo;
import org.rsfa.internal.SeasonInfo;
import org.rsfa.service.FedService;
import org.rsfa.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by radu on 3/27/17.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class LiveRestController {

  @Autowired
  FedService fedService;
  @Autowired
  LeagueService leagueService;

  @RequestMapping(value="/leagues", method = RequestMethod.GET)
  public ResponseEntity<List<FedInfo>> listAllFeds() {
    List<FedInfo> allFeds = fedService.getAllFeds();
    if(allFeds.isEmpty()){
      return new ResponseEntity<List<FedInfo>>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<List<FedInfo>>(allFeds, HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}", method = RequestMethod.GET)
  public ResponseEntity<SeasonInfo> getSeason(@PathVariable("ctty") String ctty,
                                              @PathVariable("tier") String tier,
                                              @PathVariable("season") String season) {
    return new ResponseEntity<SeasonInfo>(leagueService.getSeason(ctty, tier, season), HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}/{home}/{away}", method = RequestMethod.GET)
  public ResponseEntity<List<ResultInfo>> getResults(@PathVariable("ctty") String ctty,
                                                     @PathVariable("season") String season,
                                                     @PathVariable("tier") String tier,
                                                     @PathVariable("home") int home,
                                                     @PathVariable("away") int away) {
    List<ResultInfo> allRes = leagueService.getResults(ctty, tier, season, home, away);
    return new ResponseEntity<List<ResultInfo>>(allRes, HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}/{home}/{away}",
      method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8")
  public ResponseEntity<ResultInfo> setResults(@PathVariable("ctty") String ctty,
                                               @PathVariable("tier") String tier,
                                               @PathVariable("season") String season,
                                               @PathVariable("home") int home,
                                               @PathVariable("away") int away,
                                               @RequestBody ScoreInfo sc) {
    ResultInfo result = leagueService.setResult(ctty, tier, season, home, away, sc);
    return new ResponseEntity<ResultInfo>(result, HttpStatus.OK);
  }

  @RequestMapping(value="/leagues/{ctty}/{tier}/{season}/rounds/{round}", method = RequestMethod.GET)
  public ResponseEntity<List<ResultInfo>> getRound(@PathVariable("ctty") String ctty,
                                                   @PathVariable("tier") String tier,
                                                   @PathVariable("season") String season,
                                                   @PathVariable("round") int round) {
    List<ResultInfo> allRes = leagueService.getRound(ctty, tier, season, round);
    return new ResponseEntity<List<ResultInfo>>(allRes, HttpStatus.OK);
  }


}