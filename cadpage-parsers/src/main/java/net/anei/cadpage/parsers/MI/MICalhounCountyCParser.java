package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchH02Parser;

public class MICalhounCountyCParser extends DispatchH02Parser {
  
  public MICalhounCountyCParser() {
    super(MICalhounCountyParser.CITY_CODES, "CALHOUN COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "CCCDA-DONOTREPLY@CALHOUNCOUNTYMI.GOV";
  }
}
