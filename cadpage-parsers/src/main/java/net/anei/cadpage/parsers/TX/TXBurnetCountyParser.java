package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXBurnetCountyParser extends DispatchA72Parser {
  
  public TXBurnetCountyParser() {
    this("BURNET COUNTY", "TX");
  }
  
  public TXBurnetCountyParser(String defCity, String defState) {
    super(defCity, defState);
  }
  
  @Override
  public String getFilter() {
    return "alert@burnetsheriff.com";
  }
}
