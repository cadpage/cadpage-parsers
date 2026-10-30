package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

public class MIWayneCountyAParser extends DispatchH06Parser {

  public MIWayneCountyAParser() {
    this("WAYNE COUNTY", "MI");
  }

  MIWayneCountyAParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getFilter( ) {
    return "noreply-TylerCAD@Notifications.michigan.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
