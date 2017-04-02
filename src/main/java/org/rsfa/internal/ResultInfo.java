package org.rsfa.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.rsfa.model.results.FixtureResult;

import java.time.format.DateTimeFormatter;

/**
 * Created by radu on 3/28/17.
 */
@Data
@AllArgsConstructor
public class ResultInfo {
  private String home;
  private String away;
  private String round;
  private String date;
  private String score;

  public static ResultInfo from(final FixtureResult r, final String home, final String away) {
    return new ResultInfo(
        home,
        away,
        Integer.toString(r.getFixture().getRound()),
        r.getResult().getZ().format(DateTimeFormatter.ISO_LOCAL_DATE),
        r.getResult().toResult().toString()
    );
  }
}
