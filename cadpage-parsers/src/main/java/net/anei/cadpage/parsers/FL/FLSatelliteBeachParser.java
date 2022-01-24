package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA87Parser;

public class FLSatelliteBeachParser extends DispatchA87Parser {

  public FLSatelliteBeachParser() {
    super("SATELLITE BEACH", "FL");
  }

  @Override
  public String getFilter() {
    return "newworld@satellitebeach.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("CAD Notification")) return false;

    return super.parseMsg(body, data);
  }
}
