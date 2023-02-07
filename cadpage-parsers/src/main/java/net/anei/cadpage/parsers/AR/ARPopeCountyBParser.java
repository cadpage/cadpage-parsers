package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchA87Parser;


public class ARPopeCountyBParser extends DispatchA87Parser {

  public ARPopeCountyBParser() {
    super("POPE COUNTY", "AR");
  }

  @Override
  public String getFilter() {
    return "no-reply@popeco911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
