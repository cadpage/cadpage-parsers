package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;


public class MOMadisonCountyParser extends DispatchBCParser {
  public MOMadisonCountyParser() {
    super("MADISON COUNTY", "MO");
  }
   
  @Override
  public String getFilter() {
    return "MADISONCO911@PUBLICSAFETYSOFTWARE.NET";
  }
}
