package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class KYHendersonCountyParser extends DispatchA1Parser {

  public KYHendersonCountyParser() {
    super("HENDERSON COUNTY", "KY"); 
  }
  
  @Override
  public String getFilter() {
    return "E911@cityofhendersonky.org";
  }

}
