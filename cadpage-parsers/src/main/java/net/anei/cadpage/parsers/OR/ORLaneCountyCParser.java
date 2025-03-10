package net.anei.cadpage.parsers.OR;


import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA98Parser;



public class ORLaneCountyCParser extends DispatchA98Parser {

  public ORLaneCountyCParser() {
    super(CITY_CODES, "LANE COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "@lanecountyor.gov,@smartcop.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strPlace.equals(data.strAddress)) data.strPlace = "";
    return true;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CRE",   "CRESCENT",
      "DEX",   "DEXTER",
      "LANE",  "LANE COUNTY",
      "LOW",   "LOWELL",
      "OAK",   "OAKRIDGE",
      "WEF",   "WESTFIR"
  });
}
