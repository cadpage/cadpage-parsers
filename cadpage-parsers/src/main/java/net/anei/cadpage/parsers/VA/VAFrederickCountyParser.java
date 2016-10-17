package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAFrederickCountyParser extends DispatchOSSIParser {
  
  public VAFrederickCountyParser() {
    super("FREDERICK COUNTY", "VA",
           "ADDR CALL! X? X? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@co.frederick.va.us,CAD@psb.net,cad@fcva.us";
  }
}