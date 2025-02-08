package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA39Parser;

/**
 * Stark County C, OH
 */
public class OHStarkCountyAllianceParser extends DispatchA39Parser {

  public OHStarkCountyAllianceParser() {
    super(CITY_LIST, "STARK COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "dharpster@redcenter.us,nws@starksheriff.org,dispatch@alliancepolice.com,tacpaging.com";
  }


  @Override
  public boolean parseUntrimmedMsg(String subject, String body, Data data) {
    if (!super.parseUntrimmedMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " ALLIANCE");
    return true;
  }


  static final String[] CITY_LIST = new String[]{

      //Cities
      "ALLIANCE",
      "CANAL FULTON",
      "CANTON",
      "LOUISVILLE",
      "LOUISVILLE ALLIANCE",
      "MASSILLON",
      "NORTH CANTON",

      //Villages
      "BEACH CITY",
      "BREWSTER",
      "EAST CANTON",
      "EAST SPARTA",
      "HARTVILLE",
      "HILLS AND DALES",
      "LIMAVILLE",
      "MAGNOLIA",
      "MINERVA",
      "MEYERS LAKE",
      "NAVARRE",
      "WAYNESBURG",
      "WILMOT",

      //Townships
      "BETHLEHEM TWP",
      "CANTON TWP",
      "JACKSON TWP",
      "LAKE TWP",
      "LAWRENCE TWP",
      "LEXINGTON TWP",
      "MARLBORO TWP",
      "NIMISHILLEN TWP",
      "OSNABURG TWP",
      "PARIS TWP",
      "PERRY TWP",
      "PIKE TWP",
      "PLAIN TWP",
      "SANDY TWP",
      "SUGAR CREEK TWP",
      "TUSCARAWAS TWP",
      "WASHINGTON TWP",

      //Census-designated places
      "GREENTOWN",
      "MARLBORO",
      "NORTH LAWRENCE",
      "PERRY HEIGHTS",
      "RICHVILLE",
      "ROBERTSVILLE",
      "UNIONTOWN",

      //Other unincorporated communities
      "AVONDALE",
      "CAIRO",
      "CRYSTAL SPRINGS",
      "EAST GREENVILLE",
      "FREEBURG",
      "JUSTUS",
      "MAPLETON",
      "MARCHAND",
      "MAXIMO",
      "MCDONALDSVILLE",
      "MIDDLEBRANCH",
      "NEW BALTIMORE",
      "NEW FRANKLIN",
      "NEWMAN",
      "NORTH INDUSTRY",
      "PARIS",
      "PIGEON RUN",
      "SIPPO",
      "WACO"
    };
}

