package org.rsfa.model.league;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.rsfa.model.alias.AliasTimeline;
import org.rsfa.model.alias.Interval;
import org.rsfa.util.Syntax;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.rsfa.util.Constants.UNKNOWN;

/**
 * Created by radu on 11/30/16.
 */
@Slf4j
public class Fed {
  private @Getter int size;
  private @Getter final String ctty;
  private Club[] club;
  private Interval[] winter = null;

  public Fed(final String ct) {
    ctty = ct;
    size = 0;
  }

  public void loadClubs(final String ifile, final Syntax style) {
    try {
      final FileInputStream fstream = new FileInputStream(ifile);
      final InputStreamReader dis = new InputStreamReader(fstream, "ISO-8859-2");
      final BufferedReader br = new BufferedReader(dis);
      String s = br.readLine();
      final String[] tkh = s.split(" ");
      size = Integer.parseInt(tkh[0]);
      club = new Club[size];
      for (int i = 0; i < size; ++i) {
        s = br.readLine();
        club[i] = Club.parse(s, style);
      }
      br.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public void loadAliases(final String afile) {
    try {
      final FileInputStream fstream = new FileInputStream(afile);
      final InputStreamReader dis = new InputStreamReader(fstream, "ISO-8859-2");
      final BufferedReader br = new BufferedReader(dis);
      for (int i = 0; i < size; i++) {
        String line = br.readLine();
        if (line != null) {
          club[i].setAlias(AliasTimeline.parse(line));
        }
      }
      br.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public void loadChainedAliases(final String afile) {
    try {
      final FileInputStream fstream = new FileInputStream(afile);
      final InputStreamReader dis = new InputStreamReader(fstream, "ISO-8859-2");
      final BufferedReader br = new BufferedReader(dis);

      for (int i = 0; i < size; i++) {
        String line = br.readLine();
        if (line != null) {
          club[i].setAlias(AliasTimeline.chainedParse(line));
        }
      }
      br.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public void loadWinter(final String afile) {
    log.error("Loading winter for {} from {}", getCtty(), afile);
    try {
      final FileInputStream fstream = new FileInputStream(afile);
      final InputStreamReader dis = new InputStreamReader(fstream, "ISO-8859-2");
      final BufferedReader br = new BufferedReader(dis);
      String s = br.readLine();
      final String[] tkh = s.split(" ");
      int numW = Integer.parseInt(tkh[0]);
      winter = new Interval[numW];
      for (int i = 0; i < numW; ++i) {
        s = br.readLine();
        String tk[] = s.split(" ");
        winter[i] = Interval.range(Integer.parseInt(tk[0]), Integer.parseInt(tk[1]));
      }
      br.close();
      log.error("Loaded: {} intervals", winter.length);
      for (Interval i : winter) {
        log.error("[{} - {}]", i.getStart().getYear(), i.getEnd().getYear());
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public boolean isWinter(int y) {
    if (winter==null) return false;
    for (int i=0; i<winter.length; i++) {
      if (i>=winter[i].getStart().getYear() && i<=winter[i].getEnd().getYear()) return true;
    }
    return false;
  }

  public Collection<AliasTimeline> getAliases() {
    return Stream.of(club)
        .map(c -> c.getAlias())
        .collect(Collectors.toList());
  }

  public Club getClub(int i) {
    return (i>=0 && i<size)? club[i] : null;
  }

  public String nameOf(final int i, final int y) {
    if (i < 0 || i >= size) {
      return null;
    }
    return club[i].getAlias().getName(y);
  }

  public String nickOf(final int i, final int y) {
    if (i < 0 || i >= size) {
      return null;
    }
    return club[i].getAlias().getNick(y);
  }

  public String getMnem(final int i) {
    if (i < 0 || i >= size) {
      return null;
    }
    return club[i].getMnem();
  }

  public int findMnem(final String s) {
    // start with capital letter;
    final String us = s.substring(0, 1).toUpperCase() + s.substring(1);
    final String ts = us.replaceAll("_", " ");

    // exact match
    for (int i = 0; i < size; ++i) {
      if (ts.equals(club[i].getMnem())) {
        return i;
      }
    }

    // Substring
    for (int i = 0; i < size; ++i) {
      if (club[i].getMnem().contains(ts)) {
        return i;
      }
    }

    // try uppercase all
    final String cs = ts.toUpperCase();
    for (int i = 0; i < size; ++i) {
      if (club[i].getMnem().contains(cs)) {
        return i;
      }
    }

    return UNKNOWN;
  }

}
