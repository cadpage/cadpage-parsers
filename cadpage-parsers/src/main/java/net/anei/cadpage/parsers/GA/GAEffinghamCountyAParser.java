package net.anei.cadpage.parsers.GA;


import net.anei.cadpage.parsers.dispatch.DispatchA12Parser;


public class GAEffinghamCountyAParser extends DispatchA12Parser {
  
  public GAEffinghamCountyAParser() {
    super("EFFINGHAM COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "Effingham911@EffinghamCounty.org";
  }
  
}
