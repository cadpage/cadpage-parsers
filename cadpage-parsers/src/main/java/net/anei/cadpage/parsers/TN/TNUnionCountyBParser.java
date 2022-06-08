package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA88Parser;

public class TNUnionCountyBParser extends DispatchA88Parser {

  public TNUnionCountyBParser() {
    super(CITY_LIST, "UNION COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@unioncountytn.gov";
  }

  private static final String[] CITY_LIST = new String[] {
      // Cities
      "LUTTRELL",
      "MAYNARDVILLE",
      "PLAINVIEW",

      // Unincorporated communities
      "ALDER SPRINGS",
      "BRADEN",
      "SHARPS CHAPEL",

      // Ghost town
      "LOYSTON",

      // Knox County
      "CORRYTON"
  };

}
