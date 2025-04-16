package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class MSJacksonCountyParser extends DispatchA46Parser {

  public MSJacksonCountyParser() {
    super(CITY_LIST, "JACKSON COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "jacksoncounty@pagingpts.com,notifications@ptssolutions.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("[", "").replace("]", "").replace("\n", "");
    body = body.replace(" JACKSON, MS ", ", MS ").replace("EASTATES", "ESTATES");
    return super.parseMsg(subject, body, data);
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("GULF PARK")) city = "GULF PARK ESTATES";
    return city;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "GAUTIER",
      "MOSS POINT",
      "OCEAN SPRINGS",
      "PASCAGOULA",

      // Census-designated places
      "BIG POINT",
      "ESCATAWPA",
      "GULF HILLS",
      "GULF PARK",
      "GULF PARK ESTATES",
      "HELENA",
      "HURLEY",
      "LATIMER",
      "ST MARTIN",
      "VANCLEAVE",
      "WADE",

      // Unincorporated places
      "EAST MOSS POINT",
      "POTICAW LANDING",

      // Ghost towns
      "BREWTON",

      // Harrison County
      "BILOXI"
  };
}
