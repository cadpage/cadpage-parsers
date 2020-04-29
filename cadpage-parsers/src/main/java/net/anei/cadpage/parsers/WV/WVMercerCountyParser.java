package net.anei.cadpage.parsers.WV;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WVMercerCountyParser extends DispatchA19Parser {
  
  public WVMercerCountyParser() {
    super(CITY_CODES, "MERCER COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@mercer911.com";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BLF", "BLUEFIELD",
      "BLU", "BLUEWELL",
      "COU", "COU",
      "PRN", "PRINCETON"
  });

}
