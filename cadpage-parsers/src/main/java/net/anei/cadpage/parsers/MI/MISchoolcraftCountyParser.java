package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class MISchoolcraftCountyParser extends DispatchH03Parser {

  public MISchoolcraftCountyParser() {
    super("SCHOOLCRAFT COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "MSP@michigan.gov";
  }

}
