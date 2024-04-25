package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class WAJeffersonCountyBParser extends DispatchA57Parser {

  public WAJeffersonCountyBParser() {
    this("JEFFERSON COUNTY", "WA");
  }

  WAJeffersonCountyBParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getFilter() {
    return "Dispatch@co.clallam.wa.us,Dispatch@clallamcountywa.gov,portangeles@hiplink.com,hostedalerts@hiplink.com";
  }

}
