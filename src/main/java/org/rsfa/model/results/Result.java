package org.rsfa.model.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rsfa.util.Constants;

/**
 * Created by radu on 3/27/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
  private int x = Constants.UNKNOWN;
  private int y = Constants.UNKNOWN;

  public int pack() {
    return 100*x + y;
  }

  public static Result of(int s) {
    if (s>=0) return new Result(s/100, s%100);
    else return new Result();
  }

  public static final Result UNKNOWN = Result.of(Constants.UNKNOWN);

  public String toString() {
    return (this.equals(UNKNOWN)? "?" : String.format("%d-%d", getX(), getY()));
  }

  public String serialize() {
    return x==Constants.UNKNOWN?
        String.valueOf(Constants.UNKNOWN)
        : String.format("%d", 100*x+y);
  }
}
