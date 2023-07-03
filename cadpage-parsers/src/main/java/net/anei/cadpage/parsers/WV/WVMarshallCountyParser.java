package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class WVMarshallCountyParser extends DispatchSPKParser {

  public WVMarshallCountyParser() {
    super("MARSHALL COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "calibermail@marshallcountywv.org";
  }
}
