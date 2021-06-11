package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class CAShastaCountyCParser extends DispatchA19Parser {

  public CAShastaCountyCParser() {
    super("SHASTA COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "SHASCOM@ipsshasta.com";
  }
}
