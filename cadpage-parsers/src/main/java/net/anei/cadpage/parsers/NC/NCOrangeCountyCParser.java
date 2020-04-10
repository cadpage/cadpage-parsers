package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchH02Parser;

public class NCOrangeCountyCParser extends DispatchH02Parser {
  
  public NCOrangeCountyCParser() {
    super(CITY_CODES, "ORANGE COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "cad@orangecountync.gov";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CHAP", "CHAPEL HILL",
      "DURH", "DURHAM"
  });

}
