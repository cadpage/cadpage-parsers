package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;


public class GAColquittCountyParser extends DispatchA78Parser {

  public GAColquittCountyParser() {
    super("COLQUITT COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "911alert@interoponline.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}

