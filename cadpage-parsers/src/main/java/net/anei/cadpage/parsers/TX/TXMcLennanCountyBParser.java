package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

public class TXMcLennanCountyBParser extends DispatchA41Parser {

  public TXMcLennanCountyBParser() {
    super(CITY_CODES, "MCLENNAN COUNTY", "TX", "[A-Z]{2,3}\\d?|EOUT");
  }

  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "WAC", "WACO"
     
  });
  
  
}
