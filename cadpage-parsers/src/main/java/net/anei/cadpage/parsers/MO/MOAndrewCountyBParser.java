package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;
import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOAndrewCountyBParser extends DispatchBCParser {

  public MOAndrewCountyBParser() {
    super(CITY_LIST, "ANDREW COUNTY", "MO", DispatchA33Parser.A33_X_ADDR_EXT);
  }

  @Override
  public String getFilter() {
    return "DISPATCH@ANDREWCOUNTY.COM,NOREPLY@OMNIGO.COM";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCity = data.strCity.replace(".", "");
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BOLCKOW",
      "FILLMORE",
      "REA",
      "ROSENDALE",
      "SAVANNAH",
      "SAINT JOSEPH",
      "VILLAGES",
      "AMAZONIA",
      "COSBY",
      "COUNTRY CLUB",

      // Unincorporated communities
      "AVENUE CITY",
      "FLAG SPRINGS",
      "FOUNTAINBLEAU",
      "HELENA",
      "KODIAK",
      "NODAWAY",
      "RANKIN",
      "ROCHESTER",
      "WHITESVILLE",
      "WYTHE",

      // Townships
      "BENTON",
      "CLAY",
      "EMPIRE",
      "JACKSON",
      "JEFFERSON",
      "LINCOLN",
      "MONROE",
      "NODAWAY",
      "PLATTE",
      "ROCHESTER",
  };

}
