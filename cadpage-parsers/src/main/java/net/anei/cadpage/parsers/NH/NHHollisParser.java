package net.anei.cadpage.parsers.NH;

import net.anei.cadpage.parsers.dispatch.DispatchA16Parser;

/**
 * Hollis, NH
 */
public class NHHollisParser extends DispatchA16Parser {

  public NHHollisParser() {
    super("HCC", CITY_LIST, "HOLLIS", "NH");
  }

  @Override
  public String getFilter() {
    return "@hollisnh.org,@gmail.com";
  }

  private static final String[] CITY_LIST = new String[]{
    "BROOKLINE",
    "HOLLIS"
  };
}
  