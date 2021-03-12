package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class GAEmanuelCountyParser extends DispatchA74Parser {

  public GAEmanuelCountyParser() {
    super("EMANUEL COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "Dispatch@EmanuelE911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
