package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA11Parser;

/**
 * Monmouth County, NJ
 */
public class NJMonmouthCountyAParser extends DispatchA11Parser {

  public NJMonmouthCountyAParser() {
    super(CITY_CODES, "MONMOUTH COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "MCSOPageNotification@mcsonj.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return super.parseMsg(body, data);
  }

  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "12", "ENGLISHTOWN",
      "13", "FAIR HAVEN", // Might be SHREWSBURY
      "15", "FREEHOLD",
      "16", "FREEHOLD",
      "18", "HOLMDEL",
      "19", "HOWELL",
      "22", "KEYPORT",
      "23", "MONROE",
      "25", "LONG BRANCH",
      "26", "MANALAPAN",
      "27", "MANASQUAN",
      "28", "MARLBORO",
      "32", "MILLSTONE",
      "33", "MONMOUTH BEACH",
      "35", "NEPTUNE CITY",
      "36", "TINTON FALLS",
      "38", "OCEANPORT",
      "41", "ROOSEVELT",
      "42", "RUMSON",  // Could be KEYPORT,FAIR HAVEN,KEANSBURG
      "43", "SEA BRIGHT",
      "51", "MONROE",
      "52", "WALL TWP",
      "54", "OCEAN GROVE",
      "63", "KEYPORT",
      "65", "UNION BEACH",
      "82", "ALLENTOWN",
      "83", "ASBURY PARK",
      "84", "COLTS NECK",
      "85", "KEANSBURG",
      "86", "MATAWAN",
      "88", "BRADLEY BEACH",
      "94", "COLTS NECK",
      "99", "BRIELLE",

      "HIG", "HIGHSTOWN",
      "RB",  "ROBBINSVILLE TWP",
      "OC",  "OUT OF COUNTY"
  });
}
