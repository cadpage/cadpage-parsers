package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;




public class PAYorkCountyBParser extends DispatchA9Parser {
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "SP GARDEN TWP",    "SPRING GARDEN TWP"
  });
  
  public PAYorkCountyBParser() {
    super(CITY_CODES, "YORK COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }
}
