package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

public class MILenaweeCountyDParser extends DispatchH06Parser {

  public MILenaweeCountyDParser() {
    super("LENAWEE COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "Lenawee@lenawee.mi.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
