package org.rsfa.model.results;

/**
 * Created by radu on 3/27/17.
 */
public class HostedFixture extends Fixture {
  private int host;

  public HostedFixture(int h, int a, int w) {
    super(h, a);
    this.host = w;
  }

  public static HostedFixture ofRegular(int h, int a) {
    return new HostedFixture(h, a, h);
  }
}
