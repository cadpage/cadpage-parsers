package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCRichmondCountyParser extends DispatchSouthernParser {

  public NCRichmondCountyParser() {
    super(CITY_LIST, "RICHMOND COUNTY", "NC",
          DSFLG_ADDR | DSFLG_OPT_X | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_TIME | DSFLG_NO_INFO);
    setupCities(MISSPELLED_CITIES);
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "HAMLET",
    "ROCKINGHAM",

    // Towns
    "DOBBINS HEIGHTS",
    "ELLERBE",
    "HOFFMAN",
    "NORMAN",

    //Census-designated places
    "CORDOVA",
    "EAST ROCKINGHAM",
    "ROBERDEL",

    // Other unincorporated communities
    "FRUITLAND",
    "MARSTON",

    // Townships
    "STEELES",
    "MINERAL SPRINGS",
    "BEAVERDAM",
    "MARKS CREEK",
    "WOLF PIT",
    "ROCKINGHAM",
    "BLACKJACK"
  };

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[] {
      "DOBBIN HEIGHTS",     "DOBBINS HEIGHTS"
  });
}
