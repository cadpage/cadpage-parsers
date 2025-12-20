package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;
/**
 * Beaufort County, NC
 *
 */
public class NCBertieCountyParser extends DispatchSouthernParser {

  public NCBertieCountyParser() {
    super(CITY_LIST, "BERTIE COUNTY", "NC", 
          DSFLG_ADDR | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_TIME | DSFLG_NO_INFO);
  }

  @Override
  public String getFilter() {
    return "fireems@co.beaufort.nc.us";
  }

  private static final String[] CITY_LIST = new String[] {

      // Towns
      "ASKEWVILLE",
      "AULANDER",
      "COLERAIN",
      "KELFORD",
      "LEWISTON WOODVILLE",
      "POWELLSVILLE",
      "ROXOBEL",
      "WINDSOR",

      // Townships
      "COLERAIN",
      "INDIAN WOODS",
      "MERRY HILL",
      "MITCHELLS",
      "ROXOBEL",
      "SNAKEBITE",
      "WHITES",
      "WINDSOR",
      "WOODVILLE",

      // Unincorporated communities
      "ASHLAND",
      "AVOCA",
      "BAKER TOWN",
      "BUENA VISTA",
      "ELM GROVE",
      "GATLINSVILLE",
      "GRABTOWN",
      "GREENS CROSS",
      "HEXLENA",
      "MERRY HILL",
      "MIDWAY",
      "PERRYTOWN",
      "PINE RIDGE",
      "QUITSNA",
      "REPUBLICAN",
      "ROSEMEAD",
      "SANS SOUCI",
      "SPRING BRANCH",
      "TODDS CROSS",
      "TRAP",
      "WHITES CROSS",
      "WOODARD"
  };
}
