package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class ORCurryCountyParser extends SmartAddressParser {
  
  public ORCurryCountyParser() {
    super(CITY_LIST, "CURRY COUNTY", "OR");
    setFieldList("ADDR PLACE APT CITY CALL");
  }
  
  @Override
  public String getFilter() {
    return "station1dispatch@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    int pt = body.indexOf('/');
    if (pt < 0) return false;
    data.strCall = body.substring(pt+1).trim();
    if (data.strCall.length() == 0) data.strCall = subject;
    body = body.substring(0,pt).trim();
    
    pt = body.lastIndexOf(',');
    if (pt < 0) return false;
    data.strCity = body.substring(pt+1).trim();
    if (!isCity(data.strCity)) return false;
    if (data.strCity.equalsIgnoreCase("UNINCORPORATED")) data.strCity = "Curry County";
    body = body.substring(0,pt).trim();
    
    parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, body, data);
    return true;
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "BROOKINGS",
    "GOLD BEACH",
    "PORT ORFORD",

    // Census-designated places
    "HARBOR",
    "LANGLOIS",
    "NESIKA BEACH",
    "PISTOL RIVER",

    // Unincorporated communities
    "AGNESS",
    "BAGNELL FERRY",
    "CARPENTERVILLE",
    "DENMARK",
    "HUNTER CREEK",
    "ILLAHE",
    "MARIAL",
    "OPHIR",
    "PLUM TREES",
    "SIXES",
    "WEDDERBURN",
    
    "UNINCORPORATED"
  };
}
