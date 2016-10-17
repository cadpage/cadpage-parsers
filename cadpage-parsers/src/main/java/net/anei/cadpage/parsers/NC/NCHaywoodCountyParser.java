package net.anei.cadpage.parsers.NC;


import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCHaywoodCountyParser extends DispatchSouthernParser {
  
  public NCHaywoodCountyParser() {
    super(CITY_LIST, "HAYWOOD COUNTY", "NC", DSFLAG_LEAD_PLACE | DSFLAG_FOLLOW_CROSS | DSFLAG_ID_OPTIONAL);
  }

  @Override
  public String getFilter() {
    return "CAD@haywoodnc.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("CAD:")) body = body.substring(4).trim();
    return super.parseMsg(body, data);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return STONEY_PK_PTN.matcher(addr).replaceAll("STONEY PARK");
  }
  private static final Pattern STONEY_PK_PTN = Pattern.compile("\\bSTONEY +PK\\b", Pattern.CASE_INSENSITIVE);
  
  private static String[] CITY_LIST = new String[]{
    "CANTON",
    "CLYDE",
    "LAKE JUNALUSKA",
    "MAGGIE VALLEY",
    "WAYNESVILLE",
    "WEST CANTON"
  };
  
}