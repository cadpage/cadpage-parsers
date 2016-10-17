package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;


public class OHSugarCreekParser extends DispatchCiscoParser {
  
  public OHSugarCreekParser() {
    super("SugarCreek", "OH");
  }
  
  @Override
  public String getFilter() {
    return "CAD@sugarcreektownship.com";
  }
}
