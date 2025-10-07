package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchA98Parser;

public class FLSuwanneeCountyParser extends DispatchA98Parser {

  public FLSuwanneeCountyParser() {
    super("SUWANNEE COUNTY", "FL");
  }

  @Override
  public String getFilter() {
    return "NoReply@SuwanneeSheriff.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}
