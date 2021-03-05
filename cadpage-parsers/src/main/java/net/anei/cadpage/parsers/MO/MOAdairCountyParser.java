package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOAdairCountyParser extends DispatchBCParser {

  public MOAdairCountyParser() {
    super("ADAIR COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "ADAIR911@PUBLICSAFETYSOFTWARE.NET,adair911@itiusa.com,adair911@omnigo.com";
  }
}
