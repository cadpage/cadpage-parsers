package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class KYCaseyCountyParser extends DispatchA65Parser {

  public KYCaseyCountyParser() {
    super(CITY_LIST, "CASEY COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }

  private static final String[] CITY_LIST = new String[] {
      "BETHELRIDGE",
      "CLEMENTSVILLE",
      "CRESTON",
      "DUNNVILLE",
      "LIBERTY",
      "MIDDLEBURG",
      "PHIL",
      "JACKTOWN",
      "TEDDY",
      "UPPER TYGART",
      "WALLTOWN",
      "WINDSOR",
      "YOSEMITE"
  };

}
