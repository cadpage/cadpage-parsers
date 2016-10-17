package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class ARPulaskiCountyBParser extends DispatchA46Parser {

  public ARPulaskiCountyBParser() {
    super("PULASKI COUNTY", "AR");
    }

  @Override
  public String getFilter() {
    return "PTS@gmail.com";
  }
}
