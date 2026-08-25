package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class IDElmoreCountyParser extends DispatchA19Parser {

  public IDElmoreCountyParser() {
    super("ELMORE COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "FRN-elmorecounty@email.getrave.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
