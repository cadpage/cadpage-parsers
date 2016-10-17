
package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA53Parser;


public class TXHelotesParser extends DispatchA53Parser {

  public TXHelotesParser() {
    super(CITY_LIST, "HELOTES", "TX");
  }
  
  @Override
  public String getFilter() {
    return "@Helotes-TX.gov";
  }
  
  private static final String[] CITY_LIST = new String[]{
    "GREY FOREST",
    "HELOTES",
    "SAN ANTONIO"
  };
  
}
