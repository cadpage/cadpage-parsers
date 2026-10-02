package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class COCusterCountyParser extends DispatchA57Parser {

  public COCusterCountyParser() {
    super("CUSTER COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "crcasmtp@frecom911.com";
  }

}
