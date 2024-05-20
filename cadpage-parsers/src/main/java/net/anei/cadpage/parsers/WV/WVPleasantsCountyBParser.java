package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA95Parser;

public class WVPleasantsCountyBParser extends DispatchA95Parser {

  public WVPleasantsCountyBParser() {
    super("PLEASANTS COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "comm@pleasantscountywv911.us";
  }
}
