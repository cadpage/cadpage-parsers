package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYWebsterCountyParser extends DispatchA74Parser {

  public KYWebsterCountyParser() {
    super("WEBSTER COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "Dispatch@Providence911.info,dispatch@webster911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
