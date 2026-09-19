package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA46Parser;

public class MSJacksonCountyParser extends DispatchA46Parser {

  public MSJacksonCountyParser() {
    super(CITY_LIST, "JACKSON COUNTY", "MS");
    for (String city : CITY_LIST) {
      setupCities(city + " JACKSON", city + " MS JACKSON", city+" MSJAC");
    }
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
//    body = body.replace("[", "").replace("]", "").replace("\n", "");
//    body = body.replace(" JACKSON, MS ", ", MS ").replace("EASTATES", "ESTATES");
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " MSJAC");
    data.strCity = stripFieldEnd(data.strCity, " JACKSON");
    data.strCity = stripFieldEnd(data.strCity, " MS");
    data.strCity = data.strCity.replace("POIINT", "POINT");

    data.strSupp = stripFieldStart(data.strSupp, "JACKSON, MS");
    return true;
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
      "MOSS POIINT",   // Misspelled
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

      // George County
      "LUCEDALE",

      // Harrison County
      "BILOXI"
  };
}
