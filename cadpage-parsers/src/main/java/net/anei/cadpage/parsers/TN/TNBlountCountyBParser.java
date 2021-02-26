package net.anei.cadpage.parsers.TN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA66Parser;

public class TNBlountCountyBParser extends DispatchA66Parser {

  public TNBlountCountyBParser() {
    super(CITY_CODES, "BLOUNT COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "paging@blount911.com,messaging@blount911.com,@c-msg.org,hiplink@blount911.net,admin@blount911.com";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALC", "ALCOA",
      "FRI", "FRIENDSVILLE",
      "GR1", "GREENBACK",
      "GRE", "GREENBACK",
      "KN1", "KNOXVILLE",
      "KN2", "KNOXVILLE",
      "KN3", "KNOXVILLE",
      "LEN", "LENOIR CITY",
      "LOU", "LOUISVILLE",
      "MA1", "MARYVILLE",
      "MA2", "MARYVILLE",
      "MA3", "MARYVILLE",
      "MA4", "MARYVILLE",
      "MA5", "MARYVILLE",
      "MA6", "MARYVILLE",
      "ROC", "ROCKFORD",
      "SEY", "SEYMOUR",
      "TAL", "TALLASSEE",
      "TOW", "TOWNSEND",
      "WAL", "WALLAND"
  });
}
