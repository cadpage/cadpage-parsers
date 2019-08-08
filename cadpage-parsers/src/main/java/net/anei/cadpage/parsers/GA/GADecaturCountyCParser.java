package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class GADecaturCountyCParser extends DispatchA71Parser {
  
  public GADecaturCountyCParser() {
    super("DECATUR COUNTY", "GA");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
