package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class COCusterCountyParser extends DispatchBCParser {

  public COCusterCountyParser() {
    super("CUSTER COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

}
