package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchChiefPagingParser;

public class NJMonmouthCountyJParser extends DispatchChiefPagingParser {

  public NJMonmouthCountyJParser() {
    super(CITY_LIST, "MONMOUTH COUNTY", "NJ");
  }


  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.toUpperCase().endsWith(" BORO")) {
      data.strCity = data.strCity.substring(0,data.strCity.length()-5);
    }
    return true;
  }


  private static final String[] CITY_LIST = new String[] {
      "ABERDEEN",
      "ALLENHURST",
      "ALLENTOWN",
      "ASBURY PARK",
      "ATLANTIC HIGHLANDS",
      "AVON BY THE SEA",
      "BELMAR",
      "BRADLEY BEACH",
      "BRIELLE",
      "COLTS NECK",
      "DEAL",
      "EATONTOWN",
      "ENGLISHTOWN",
      "FAIR HAVEN",
      "FARMINGDALE",
      "FREEHOLD",
      "FREEHOLD BORO",
      "FREEHOLD TOWNSHIP",
      "HAZLET",
      "HIGHLANDS",
      "HOLMDEL",
      "HOWELL",
      "INTERLAKEN",
      "KEANSBURG",
      "KEYPORT",
      "LAKE COMO",
      "LITTLE SILVER",
      "LOCH ARBOUR",
      "LONG BRANCH",
      "MANALAPAN",
      "MANASQUAN",
      "MARLBORO",
      "MATAWAN",
      "MIDDLETOWN",
      "MILLSTONE TOWNSHIP",
      "MONMOUTH BEACH",
      "MORGANVILLE",
      "NEPTUNE CITY",
      "NEPTUNE TOWNSHIP",
      "OCEAN TOWNSHIP",
      "OCEANPORT",
      "RED BANK",
      "ROOSEVELT",
      "RUMSON",
      "SEA BRIGHT",
      "SHREWSBURY",
      "SHREWSBURY BORO",
      "SHREWSBURY TOWNSHIP",
      "SPRING LAKE",
      "SPRING LAKE HEIGHTS",
      "TINTON FALLS",
      "UNION BEACH",
      "UPPER FREEHOLD",
      "WALL",
      "WEST LONG BRANCH"


  };
}
