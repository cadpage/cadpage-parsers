package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA11Parser;

public class CASutterCountyParser extends DispatchA11Parser {

  public CASutterCountyParser() {
    super(CITY_CODES, "SUTTER COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "Dispatch@yubacity.net";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "KL",  "KNIGHTS LANDING",
      "LO",  "LIVE OAK",
      "MER", "MERIDIAN",
      "NIC", "NICOLAUS",
      "PG",  "PLEASANT GROVE",
      "RIO", "RIO LINDA",
      "ROB", "ROBBINS",
      "SUT", "SUTTER COUNTY",
      "YC",  "YUBA CITY",
      "YC3", "YUBA CITY"
  });
}
