package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;



public class OHDelawareCountyAParser extends DispatchA1Parser {
  
  public OHDelawareCountyAParser() {
    super("DELAWARE COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "del-911@co.delaware.oh.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.length() == 0 && data.strApt.contains(" - ")) {
      data.strCity = data.strApt;
      data.strApt = "";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("APT", "APT CITY");
  }

  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("S OLD 3C HWY", "S OLD 3C RD");
  }
  
  @Override
  public String adjustMapCity(String city) {
    int pt = city.indexOf(" - ");
    if (pt < 0) return city;
    String temp = MAP_CITY_TABLE.getProperty(city);
    if (temp != null) return temp;
    return city.substring(0,pt) + " COUNTY";
  }
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "FRANKLIN - DUBLIN",    "DUBLIN,FRANKLIN COUNTY",
      "KNOX - HILLIAR",       "HILLIAR TOWNSHIP,KNOX COUNTY",
      "LICKING - JERSEY",     "JERSEY TOWNSHIP,LICKING COUNTY",
      "MORROW - BENNINGTON",  "BENNINGTON TOWNSHIP,MORROW COUNTY",
      "MORROW - LINCOLN",     "LINCOLN TOWNSHIP,MORROW COUNTY",
      "MORROW - PERU",        "MORROW COUNTY",
      "MORROW - WESTFIELD",   "WESTFIELD TOWNSHIP,MORROW COUNTY",
      "UNION - JEROME",       "UNION COUNTY"
  });
}
