package org.rsfa.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsfa.internal.FedInfo;
import org.rsfa.internal.LeagueInfo;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by radu on 3/27/17.
 */
@Slf4j
@NoArgsConstructor
public class InMemoryFedService implements FedService {

  private List<FedInfo> feds = null;

  @Override
  public List<FedInfo> getAllFeds() {
    if (feds==null) loadFromDisk();
    return feds;
  }

  private void loadFromDisk() {
    feds = new ArrayList<FedInfo>();
    FedInfo fed = null;
    String dir = null;
    int t = 0;
    int l = 0;
    try {
      log.error("Loading from " + repo + leagueNamesFile);
      final FileInputStream fstream = new FileInputStream(repo + leagueNamesFile);
      final DataInputStream dis = new DataInputStream(fstream);
      final BufferedReader br = new BufferedReader(new InputStreamReader(dis));
      String numFedsStr = br.readLine().trim();
      int nf = Integer.parseInt(numFedsStr);
      while (t <= nf) {
        String s = br.readLine().trim();
        if (s.charAt(0) == '#' || s.charAt(0) == '$') {
          t++;
          if (fed!=null) {
            feds.add(fed);
            l = 0;
          }
          String[] tk = s.substring(1).split("[|]");
          if (tk.length < 3) {
            log.error("Invalid fed info: " + s);
          } else {
            dir = tk[2];
            fed = new FedInfo(tk[0], tk[1], tk[2], new ArrayList<LeagueInfo>());
          }
        } else {
          String[] ltk = s.split(":");
          if (ltk.length < 2) {
            log.error("Invalid league info: " + s);
          } else {
            String ln = ltk[1];
            String group = "";
            if (ltk[1].contains("|")) {
              String[] ptk = ltk[1].split("[|]");
              ln = ptk[0];
              group = ptk[1];
            }
            String lid = String.format("%s%03d", dir, l);
            LeagueInfo lg = new LeagueInfo(lid, dir, ln, group, ltk[0].substring(0,1), ltk[0].substring(1));
            l++;
            fed.addLeague(lg);
          }
        }
      }
    } catch (Exception e) {
      log.error("Cannot load league names", e.getMessage());
    }

  }

  @Value("${repo}")
  private String repo;

  @Value("${league.names}")
  private String leagueNamesFile;

}
