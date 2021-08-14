package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class NJMonmouthCountyGParser extends DispatchA32Parser {

  public NJMonmouthCountyGParser() {
    super(NJMonmouthCountyParser.CITY_LIST, "MONMOUTH COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "howellnjpolice@gmail.com";
  }

}
