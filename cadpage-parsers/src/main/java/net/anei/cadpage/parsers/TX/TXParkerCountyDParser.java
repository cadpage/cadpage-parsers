package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXParkerCountyDParser extends DispatchA18Parser {
  
  public TXParkerCountyDParser() {
    super(CITY_LIST, "PARKER COUNTY","TX");
  }
 
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\n___");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (subject.length() > 0 && body.startsWith("-")) {
      body = subject + '\n' + body;
    }
    return super.parseMsg(body, data);
  }

  @Override
  public String getFilter() {
    return "mbaldwin@weatherfordtx.gov,pddispatch@weatherfordtx.gov";
  }

  private static String[] CITY_LIST = new String[]{
      "ADELL",
      "AGNES",
      "ALEDO",
      "ANNETTA",
      "ANNETTA NORTH",
      "ANNETTA SOUTH",
      "AZLE",
      "BRIAR",
      "BROCK",
      "COOL",
      "CRESSON",
      "DENNIS",
      "GARNER",
      "HUDSON OAKS",
      "HORSESHOE BEND",
      "MILLSAP",
      "MINERAL WELLS",
      "PEASTER",
      "POOLVILLE",
      "RENO",
      "SANCTUARY",
      "SPRINGTOWN",
      "WEATHERFORD",
      "WESTERN LAKE",
      "WHITT",
      "WILLOW PARK"
  };
}
  
