package org.rsfa.model.league;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.rsfa.model.alias.AliasTimeline;
import org.rsfa.util.Syntax;

/**
 * Created by radu on 3/27/17.
 */
@AllArgsConstructor
public class Club {
  @Getter
  private final String name;
  @Getter
  private final String mnem;
  @Getter @Setter
  private AliasTimeline alias;

  public static Club parse(String s, Syntax style) {
    if (style.equals(Syntax.FIXED)) {
      return new Club(s.substring(15), s.substring(0, 15).trim(), null);
    } else {
      final String[] tkc = s.split(",");
      return new Club(tkc[1], tkc[0].trim(), null);
    }

  }
}
