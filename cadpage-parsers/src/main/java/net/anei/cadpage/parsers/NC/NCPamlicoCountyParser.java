package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchX01Parser;

public class NCPamlicoCountyParser extends DispatchX01Parser {

  public NCPamlicoCountyParser() {
    super(CITY_CODES, "PAMLICO COUNTY", "NC");
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ARP", "ARAPAHOE",
      "BAY", "BAYBORO",
      "NBN", "NEW BERN",
      "ORI", "ORIENTAL"
  });
}
