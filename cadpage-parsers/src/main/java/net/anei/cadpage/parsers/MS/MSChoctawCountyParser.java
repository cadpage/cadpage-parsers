package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class MSChoctawCountyParser extends DispatchA74Parser {

  public MSChoctawCountyParser() {
    super("CHOCTAW COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "Dispatch@ChoctawMSE911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    body = body.replace(" ACROSS FROM ", " ACROSS ");
    return super.parseMsg(subject, body, data);
  }
}
