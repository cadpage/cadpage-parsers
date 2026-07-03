package net.anei.cadpage.parsers.OK;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchX01Parser;


public class OKBryanCountyAParser extends DispatchX01Parser {

  public OKBryanCountyAParser() {
    super(CITY_CODES, "BRYAN COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "@durant.org,lakewood1051@yahoo.com,15803801917@tmomail.net";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ACH", "ACHILLE",
      "ALB", "ALBANY",
      "ARM", "ARMSTRONG",
      "BEN", "BENNINGTON",
      "BLU", "BLUE",
      "BOK", "BOKCHITO",
      "CAD", "CADDO",
      "CAL", "CALERA",
      "CAR", "CARTWRIGHT",
      "COL", "COLBERT",
      "DUR", "DURANT",
      "HEN", "HENDRICK",
      "KEN", "KENEFIC",
      "KIN", "KINGSTON",
      "MEA", "MEAD",
      "PLA", "PLATTER",
      "ROB", "ROBERTA",
      "SIL", "SILO",
      "UTI", "UTICA",
      "YUB", "YUBA"

  });
}
