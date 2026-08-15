package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

/**
 * McDuffie County, GA
 */
public class GAMcDuffieCountyParser extends DispatchA78Parser {

  public GAMcDuffieCountyParser() {
    super("MCDUFFIE COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "donotreply@McDuffieSOalerts.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
