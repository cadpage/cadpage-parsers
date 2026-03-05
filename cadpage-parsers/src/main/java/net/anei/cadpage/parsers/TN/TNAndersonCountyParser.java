package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchC04Parser;


public class TNAndersonCountyParser extends DispatchC04Parser {

  public TNAndersonCountyParser() {
    super("ANDERSON COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "ISOMSALERT@ANDERSONCOUNTYTN.GOV";
  }
}
