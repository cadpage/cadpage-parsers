package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class TXRandallCountyParser extends DispatchH03Parser {

  public TXRandallCountyParser() {
    super(CITY_CODES, "RANDALL COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "RCSO@rc-sheriff.com";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AMAR", "AMARILLO",
      "BUSH", "BUSHLAND",
      "CNYN", "CANYON",
      "LKTW", "LAKE TANGLEWOOD",
      "POTT", "POTTER CO",
      "RAND", "RANDALL",
      "TIMB", "TIMBERCREEK"

  });

}
