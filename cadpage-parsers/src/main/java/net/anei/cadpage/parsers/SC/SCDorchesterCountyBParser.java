package net.anei.cadpage.parsers.SC;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class SCDorchesterCountyBParser extends DispatchA19Parser {

  public SCDorchesterCountyBParser() {
    super(CITY_CODES, "DORCHESTER COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "spdnotify@summervillesc.gov,sds@smvpd-spillman.police.local";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BCO", "SUMMERVILLE",
      "DCO", "SUMMERVILLE",
      "LIN", "SUMMERVILLE",
      "SUM", "SUMMERVILLE"
  });

}
