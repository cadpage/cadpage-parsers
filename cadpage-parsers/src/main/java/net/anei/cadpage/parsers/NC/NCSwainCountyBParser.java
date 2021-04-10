package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCSwainCountyBParser extends DispatchSouthernParser {

  public NCSwainCountyBParser() {
    super(CITY_LIST, "SWAIN COUNTY", "NC",
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_X | DSFLG_NAME | DSFLG_PHONE | DSFLG_ID | DSFLG_TIME);
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final String[] CITY_LIST = new String[] {
      // Town
      "BRYSON CITY",

      // Census-designated place
      "QUALLA BOUNDARY",
      "CHEROKEE",

      // Unincorporated communities
      "ALARKA",
      "ALMOND",
      "BRUSH CREEK",
      "DEALS GAP",
      "ELA",
      "HEWITT",
      "LAUADA",
      "NEEDMORE",
      "RAVENSFORD",
      "WESSER",
      "WHITTIER",
      "TOWNSHIPS",
      "CHARLESTON",
      "FORNEY CREEK",
      "GRASSY BRANCH",
      "NANTAHALA"
  };

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[] {
      "GRASSY BRANCH",      "BRYSON CITY",
      "NEEDMORE",           "BRYSON CITY"
  });
}
