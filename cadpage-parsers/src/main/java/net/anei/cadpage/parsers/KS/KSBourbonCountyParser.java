package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class KSBourbonCountyParser extends DispatchBCParser {

  public KSBourbonCountyParser() {
    super("BOURBON COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "ACTIVE911@FSCITY.ORG,ACTIVE911@OMNIGO.COM";
  }

}
