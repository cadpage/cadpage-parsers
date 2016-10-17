package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class MOLivingstonCountyParser extends DispatchA25Parser {
  
  public MOLivingstonCountyParser() {
    super("LIVINGSTON COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "police@chillimopd.org";
  }
}
