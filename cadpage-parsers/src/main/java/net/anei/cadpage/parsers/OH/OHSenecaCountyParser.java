package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;

public class OHSenecaCountyParser extends DispatchA63Parser {

  public OHSenecaCountyParser() {
    super(CITY_CODES, CITY_LIST, "SENECA COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "tpdblotter@tiffinohio.gov";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BELLEVUE",
      "FOSTORIA",
      "TIFFIN",

      // Villages
      "ATTICA",
      "BETTSVILLE",
      "BLOOMVILLE",
      "GREEN SPRINGS",
      "NEW RIEGEL",
      "REPUBLIC",

      // Townships
      "ADAMS",
      "BIG SPRING",
      "BLOOM",
      "CLINTON",
      "EDEN",
      "HOPEWELL",
      "JACKSON",
      "LIBERTY",
      "LOUDON",
      "PLEASANT",
      "REED",
      "SCIPIO",
      "SENECA",
      "THOMPSON",
      "VENICE",

      // Census-designated places
      "BASCOM",
      "FLAT ROCK",
      "FORT SENECA",
      "KANSAS",
      "MCCUTCHENVILLE",
      "MELMORE",
      "OLD FORT",

      // Unincorporated communities
      "ADRIAN",
      "ALVADA",
      "AMSDEN",
      "ANGUS",
      "BERWICK",
      "CAROLINE",
      "CARROTHERS",
      "COOPER",
      "CROMERS",
      "FIRESIDE",
      "FRENCHTOWN",
      "ILER",
      "LOWELL",
      "MAPLE GROVE",
      "OMAR",
      "REEDTOWN",
      "REHOBOTH",
      "ROCKAWAY",
      "SAINT STEPHENS",
      "SIAM",
      "SPRINGVILLE",
      "SWANDER",
      "WATSON",
      "WEST LODI"

  };

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "PD", "Tiffin"
  });
}
