package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchC04Parser;

public class OKTulsaCountyParser extends DispatchC04Parser {

  public OKTulsaCountyParser() {
    super("TULSA COUNTY", "OK");
  }

  @Override
  public String getFilter() {
    return "NO-REPLY@OKBIXBYPD.MMMICRO.COM";
  }
}
