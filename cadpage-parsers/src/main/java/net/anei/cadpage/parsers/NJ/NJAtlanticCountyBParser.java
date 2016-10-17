package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class NJAtlanticCountyBParser extends DispatchProphoenixParser {
  
  public NJAtlanticCountyBParser() {
    super(CITY_CODES,"ATLANTIC COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "CADAlert@townshipofhamilton.com";
  }
  
  @Override
  public String adjustMapAddress(String address) {
    address = stripFieldStart(address, "AREA ");
    return super.adjustMapAddress(address);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "HT", "HAMILTON",
      "ht", "HAMILTON"
  });
}
