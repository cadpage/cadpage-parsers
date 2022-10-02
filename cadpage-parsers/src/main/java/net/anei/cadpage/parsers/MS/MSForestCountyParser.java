package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MSForestCountyParser extends DispatchA48Parser {

  public MSForestCountyParser() {
    super(CITY_LIST, "FOREST COUNTY", "MS", FieldType.INFO, A48_OPT_CALL);
  }

  @Override
  public String getFilter() {
    return "@co.forrest.ms.us";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "HATTIESBURG",
      "PETAL",

      // Census-designated places
      "EASTABUCHIE",
      "GLENDALE",
      "RAWLS SPRINGS",

      // Other unincorporated communities
      "BROOKLYN",
      "CARNES",
      "FRUITLAND PARK",
      "MAXIE",
      "MAYBANK",
      "MCLAURIN",
      "WALLIS",

      // Ghost towns
      "RIVERSIDE"
  };
}
