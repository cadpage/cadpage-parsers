package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class MIOgemawCountyParser extends DispatchA19Parser {

  public MIOgemawCountyParser() {
    super("OGEMAW COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "noreply@iosco911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
