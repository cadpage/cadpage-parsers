package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class NCEdgecombeCountyCParser extends DispatchA71Parser {

  public NCEdgecombeCountyCParser() {
    super("EDGECOMBE COUNTY", "NC");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern CITY_TWP_PTN = Pattern.compile("\\d{1,2}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject,  body, data)) return false;
    if (CITY_TWP_PTN.matcher(data.strCity).matches()) data.strCity = "TOWNSHIP " + data.strCity;
    return true;
  }
}
