package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VABuckinghamCountyBParser extends DispatchA71Parser {

  public VABuckinghamCountyBParser() {
    super("BUCKINGHAM COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }
}
