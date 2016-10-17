package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;


public class NCLumbertonParser extends DispatchA47Parser {
  
  public NCLumbertonParser() {
    super("Comm CFS info", CITY_LIST, "LUMBERTON", "NC", null);
  }
  
  @Override
  public String getFilter() {
    return "swmail@ci.lumberton.nc.us";
  }

  private static final String[] CITY_LIST =new String[]{
    "LUMBERTON"
  };
}
