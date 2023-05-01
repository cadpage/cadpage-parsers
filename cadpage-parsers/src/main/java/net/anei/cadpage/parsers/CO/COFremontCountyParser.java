package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class COFremontCountyParser extends DispatchBCParser {


  public COFremontCountyParser() {
    super("FREMONT COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }
}
