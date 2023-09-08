package net.anei.cadpage.parsers.GA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class GACherokeeCountyBParser extends DispatchH03Parser {

  public GACherokeeCountyBParser() {
    super(CITY_CODES, "CHEROKEE COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return ".911@cherokeega.com";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AC", "ACWORTH",
      "BG", "BALL GROUND",
      "CA", "CANTON",
      "CH", "CHEROKEE COUNTY",
      "RO", "ROSWELL",
      "WA", "WALESKA",
      "WH", "WHITE",
      "WO", "WOODSTOCK"
  });
}
