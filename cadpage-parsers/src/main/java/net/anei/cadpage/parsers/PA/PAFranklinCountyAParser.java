package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA94Parser;

/**
 * Franklin County, PA
 */
public class PAFranklinCountyAParser extends DispatchA94Parser {

  public PAFranklinCountyAParser() {
    super("FRANKLIN COUNTY", "PA");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
