package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class CAMercedCountyParser extends DispatchA20Parser {

  public CAMercedCountyParser() {
    super("MERCED COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "@losbanos.org";
  }

}
