package org.rsfa.model.league;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.rsfa.model.results.Fixture;
import org.rsfa.model.results.FixtureResult;
import org.rsfa.model.results.Results;
import org.rsfa.model.results.TimedResult;
import org.rsfa.model.stats.Stat;
import org.rsfa.model.stats.StatSorter;
import org.rsfa.util.Constants;

import java.io.*;
import java.util.Collection;
import java.util.StringTokenizer;

import static org.rsfa.util.Constants.UNKNOWN;

/**
 * Created by radu on 11/30/16.
 */
@Slf4j
@NoArgsConstructor
public class League {
  @Setter @Getter private Fed fed;
  @Setter @Getter private String filename;
  @Getter @Setter private LeagueFormat format;
  @Getter @Setter private LeagueMetadata metadata;
  @Getter @Setter private int size = 0;
  @Getter private Integer[] id;
  @Getter private Integer[] rank;
  @Getter private Stat[] stat;
  @Getter @Setter private String[] deco;
  @Getter @Setter private Integer[] pdt;
  @Getter private Results res;
  @Getter private Integer numr;

  public League(Fed f) {
    fed = f;
  }

  public void load(String ifile) {
    size = 0;
    filename = ifile;
    int year = 0;
    try {
      String tkf[] = ifile.split("\\.");
      year = Integer.parseInt(tkf[1]);
      setMetadata(new LeagueMetadata(year, tkf[0], 0));

      final FileInputStream fstream = new FileInputStream(ifile);
      final DataInputStream dis = new DataInputStream(fstream);
      final BufferedReader br = new BufferedReader(new InputStreamReader(dis));

      String line = br.readLine().trim();
      final String[] tkh = line.split(" ");

      size = Integer.parseInt(tkh[0]);
      format = new LeagueFormat();
      id = new Integer[size];
      rank = new Integer[size];
      stat = new Stat[size];
      pdt = new Integer[size];
      deco = new String[size];

      format.setPpv(Integer.parseInt(tkh[1]));
      int tb = Integer.parseInt(tkh[2]);
      numr = 1 + tb/Constants.NUMORD;
      format.setSize(size);
      format.setRounds(numr);
      format.setTbr(tb);
      Integer[] promo = new Integer[2];
      Integer[] releg = new Integer[2];
      promo[0] = Integer.parseInt(tkh[3]);
      promo[1] = Integer.parseInt(tkh[4]);
      releg[0] = Integer.parseInt(tkh[5]);
      releg[1] = Integer.parseInt(tkh[6]);
      format.setPromo(promo);
      format.setReleg(releg);
      format.setWinter(fed.isWinter(year));
      log.error("{} {}: {}", fed.getCtty(), year, format.getWinter()?"winter":"fall");

      for (int i = 0; i < size; i++) {
        stat[i] = new Stat();
        line = br.readLine().trim();
        final StringTokenizer tkt = new StringTokenizer(line);
        final int ntk = tkt.countTokens();
        if (ntk >= 8) {
          final String sid = tkt.nextToken();
          final int x = Integer.parseInt(sid);
          id[i] = x;
          stat[i].setId(id[i]);
          tkt.nextToken(); // dummy
          int w = Integer.parseInt(tkt.nextToken());
          int d = Integer.parseInt(tkt.nextToken());
          int l = Integer.parseInt(tkt.nextToken());
          int s = Integer.parseInt(tkt.nextToken());
          int r = Integer.parseInt(tkt.nextToken());
          tkt.nextToken(); // pts

          if (ntk >= 10) {
            stat[i].setPen(Integer.parseInt(tkt.nextToken()));
            pdt[i] = Integer.parseInt(tkt.nextToken());
          } else {
            pdt[i] = 0;
          }
          if (ntk >= 11) deco[i] = tkt.nextToken(); else deco[i] = "";
          rank[i] = i;
        }
      }

      res = new Results(size, numr);

      for (int k = 0; k < numr; k++) {
        for (int i = 0; i < size; i++) {
          line = br.readLine().trim();
          final String[] tkr = line.split(" +");
          if (tkr.length < 2 * size) {
            System.out.println(
                String.format("Input results error on line {}, {} tokens instead of {}",
                i, tkr.length, 2 * size));
          }
          for (int j = 0; j < size; j++) {
            if (2 * j + 1 < tkr.length) {
              int rz = Integer.parseInt(tkr[2 * j]);
              int s = Integer.parseInt(tkr[2 * j + 1]);
              FixtureResult fr = FixtureResult.from(i, j, metadata.getYear(), rz, s);
              if (s>=0) { res.add(fr); }
            }
          }
        }
      }
      dis.close();
      if (!getFormat().getWinter()) { getRes().adjustYear(); }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public void sort() {
    StatSorter ss = new StatSorter(this);
    ss.sort();
  }

  public boolean save() {
    String newfilename = filename + ".new";
    try {
      String payload = this.serialize();
      PrintWriter writer = new PrintWriter(newfilename);
      writer.print(payload);
      writer.close();
      File oldFile = new File(filename);
      File newFile = new File(newfilename);
      File backupFile = new File(filename + ".old");
      if (!oldFile.renameTo(backupFile))  return false;
      if (!newFile.renameTo(oldFile)) return false;
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  public int findMnem(final String s) {
    // start with capital letter;
    final String us = s.substring(0, 1).toUpperCase() + s.substring(1);
    final String ts = us.replaceAll("_", " ");

    // exact match
    for (int i = 0; i < size; ++i) {
      if (ts.equals(fed.getClub(id[i]).getMnem())) {
        return i;
      }
    }

    // Substring
    for (int i = 0; i < size; ++i) {
      if (fed.getClub(id[i]).getMnem().contains(ts)) {
        return i;
      }
    }

    // try uppercase all
    final String cs = ts.toUpperCase();
    for (int i = 0; i < size; ++i) {
      if (fed.getClub(id[i]).getMnem().contains(cs)) {
        return i;
      }
    }

    return UNKNOWN;
  }

  public int findId(int x) {
    for (int i=0; i<size; i++) {
      if (id[i] == x) return i;
    }
    return Constants.UNKNOWN;
  }

  public String nameOf(int i) {
    return fed.nameOf(id[i], metadata.getYear());
  }

  public String nickOf(int i) {
    return fed.nickOf(id[i], metadata.getYear());
  }

  public int rankOf(int t) {
    for (int i=0; i<size; i++) {
      if (rank[i]==t) return i;
    }
    return Constants.UNKNOWN;
  }

  public String toString(FixtureResult fr) {
    Fixture f = fr.getFixture();
    TimedResult r = (TimedResult) fr.getResult();
    return String.format("[R%2d] %s: %-15s %d-%d %-15s",
        f.getRound(), r.getZ(),
        nickOf(f.getHome()),
        r.getX(),
        r.getY(),
        nickOf(f.getAway())
    );
  }

  public String toString(Collection<FixtureResult> fr) {
    StringBuilder sb = new StringBuilder();
    fr.forEach(r -> sb.append(toString(r)+"\n"));
    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    int p1 = format.getPromo()[0];
    int p2 = format.getPromo()[1];
    int r1 = format.getReleg()[0];
    int r2 = format.getReleg()[1];
    for (int i=0; i<size; i++) {
      int x = getRank()[i];
      sb.append((i+1) + ".");
      sb.append(String.format("%-30s", fed.nameOf(id[x], metadata.getYear())));
      sb.append("\t");
      sb.append(stat[x].toString(format.getPpv()));

      sb.append(deco[x]);
      sb.append("\n");
      if (i==p1-1) sb.append(Constants.SINGLE_LINE_CONT);
      if (i==p1+p2-1 && p2>0) sb.append(Constants.SINGLE_LINE_DASH);
      if (i==size-(r1+r2)-1 && r2>0) sb.append(Constants.SINGLE_LINE_DASH);
      if (i==size-r1-1 && r1>0) sb.append(Constants.SINGLE_LINE_CONT);
    }
    return sb.toString();
  }

  public String statLine(int i) {
    StringBuilder sb = new StringBuilder();
    sb.append(stat[i].serialize(format.getPpv()));
    if (stat[i].getPen()!=0 || deco[i].length()>0) {
      sb.append(String.format(" %3d %3d", stat[i].getPen(), pdt[i]));
    }
    if (deco[i].length()>0) {
      sb.append(" " + deco[i]);
    }
    return sb.toString();
  }

  public void addResult(FixtureResult fr) {
    res.add(fr);
  }

  public FixtureResult getResult(int i, int j, int k) {
    return getRes().getResult(i, j, k);
  }

  public void deleteResult(FixtureResult fr) { res.delete(fr); }

  public void countResult(FixtureResult fr) {
    int i = fr.getFixture().getHome();
    int j = fr.getFixture().getAway();
    int s = fr.getPacked();
    stat[i].addResult(s);
    stat[j].addReverseResult(s);
  }

  public void discountResult(FixtureResult fr) {
    int i = fr.getFixture().getHome();
    int j = fr.getFixture().getAway();
    int s = fr.getPacked();
    stat[i].deleteResult(s);
    stat[j].deleteReverseResult(s);
  }

  public void countAllResults() {
    res.getAll().stream().forEach(this::countResult);
  }

  public void resetStats() {
    for (Stat s : getStat()) {
      s.reset();
    }
  }

  public String results(int i, int k) {
    return res.serialize(i, k);
  }

  public String serialize() {
    StringBuilder sb = new StringBuilder();
    sb.append(getFormat().toString()+"\n");
    for (int i=0; i<size; i++) {
      sb.append(statLine(i)+"\n");
    }
    sb.append(res.serialize());
    return sb.toString();
  }
}
