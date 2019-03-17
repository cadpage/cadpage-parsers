package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOMadisonCountyParser extends DispatchA33Parser {
  public MOMadisonCountyParser() {
    super("MADISON COUNTY", "MO");
  }
   
  @Override
  public String getFilter() {
    return "MADISONCO911@OMNIGO.COM,MADISONCO911@PUBLICSAFETYSOFTWARE.NET";
  }
}
