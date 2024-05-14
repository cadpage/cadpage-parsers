package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class COFremontCountyParser extends DispatchA57Parser {


  public COFremontCountyParser() {
    super("FREMONT COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "crcasmtp@hamilton.net";
  }
}
