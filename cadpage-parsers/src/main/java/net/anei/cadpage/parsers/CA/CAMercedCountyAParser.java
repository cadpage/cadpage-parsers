package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class CAMercedCountyAParser extends DispatchA20Parser {

  public CAMercedCountyAParser() {
    super("MERCED COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "@losbanos.org";
  }

}
