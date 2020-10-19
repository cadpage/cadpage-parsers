package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class WVNicholasCountyParser extends DispatchEmergitechParser {
  
  public WVNicholasCountyParser() {
    super(CITY_LIST, "NICHOLAS COUNTY", "WV");
  }
  
  @Override
  public String getFilter() {
    return "NCDHSEM@shentel.net";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CAD TEXT|")) return false;
    subject = subject.substring(9);
    return super.parseMsg(subject, body, data);
  }

  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "RICHWOOD",
      "SUMMERSVILLE",

      // Magisterial districts
      "BEAVER",
      "GRANT",
      "HAMILTON",
      "JEFFERSON",
      "KENTUCKY",
      "SUMMERSVILLE",
      "WILDERNESS",

      // Census-designated places
      "BELVA",
      "BIRCH RIVER",
      "CRAIGSVILLE",
      "DIXIE",
      "FENWICK",
      "NETTIE",
      "TIOGA",

      // Unincorporated communities
      "BENTREE",
      "CALVIN",
      "CAMBRIA",
      "CANVAS",
      "COTTLE",
      "DRENNEN",
      "ENON",
      "GILBOA",
      "HOLCOMB",
      "HOOKERSVILLE",
      "KESSLERS CROSS LANES",
      "LEIVASY",
      "LOCKWOOD",
      "MOUNT NEBO",
      "MOUNT LOOKOUT",
      "MUDDLETY",
      "NEW HOPE",
      "ODELL TOWN",
      "PERSINGER",
      "POOL",
      "SWISS",
      "WERTH",
      "ZELA"
  };
}
