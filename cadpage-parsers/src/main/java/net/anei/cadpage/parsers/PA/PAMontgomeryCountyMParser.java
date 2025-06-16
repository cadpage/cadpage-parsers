package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA92Parser;

public class PAMontgomeryCountyMParser extends DispatchA92Parser {

  public PAMontgomeryCountyMParser() {
    super("MONTGOMERY COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "sa@logis.dk";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
