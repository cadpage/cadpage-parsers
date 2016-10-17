package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernPlusParser;


public class TNLoudonCountyParser extends DispatchSouthernPlusParser {
  
  
  public TNLoudonCountyParser() {
    super(CITY_LIST, "LOUDON COUNTY", "TN");
  }
  
  @Override
  public String getFilter() {
    return "@loudoncounty911.org,ldn911txt@gmail.com,777,9300,4702193752,8573031955,4702193970,2183500369,2183500365";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (body.startsWith("LDN911 ") || body.startsWith("LDN911:")) body = body.substring(7).trim();
    body = body.replaceAll("//+", "/");
    return super.parseMsg(subject, body, data);
  }
  

  private static final String[] CITY_LIST = new String[]{
    "GREENBACK",
    "LENOIR CITY",
    "LOUDON",
    "PHILADELPHIA",
    
    "DIXIE LEE JUNCTION",
    "MORGANTON",
    "TELLICO VILLAGE"
  };

}
