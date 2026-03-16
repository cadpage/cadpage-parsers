package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

public class GAUnionCountyParser extends DispatchA86Parser {

  public GAUnionCountyParser() {
    super("UNION COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "dispatch@unioncounty-ga-911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
