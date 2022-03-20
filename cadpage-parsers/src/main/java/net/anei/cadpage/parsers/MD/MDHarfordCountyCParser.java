package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MDHarfordCountyCParser extends DispatchA48Parser {

  public MDHarfordCountyCParser() {
    super(MDHarfordCountyParser.CITY_LIST, "HARFORD COUNTY", "MD", FieldType.X, A48_NO_CODE);
  }

  @Override
  public String getFilter() {
    return "EMS-YORKSOUTHCOUNTY@harfordcountymd.gov";
  }

}
