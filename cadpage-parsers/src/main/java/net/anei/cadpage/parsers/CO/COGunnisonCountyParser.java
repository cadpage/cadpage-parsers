package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class COGunnisonCountyParser extends DispatchA33Parser {

  public COGunnisonCountyParser() {
    this("GUNNISON COUNTY");
  }

  public COGunnisonCountyParser(String county) {
    super(county, "CO");
  }

  @Override
  public String getAliasCode() {
    return "COGunnisonCounty";
  }

  @Override
  public String getFilter() {
    return "DISPATCHCENTER@CITYOFGUNNISON-CO.GOV,DISPATCHCENTER@GUNNISONCO.GOV";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    body = fixit(body);
    return super.parseMsg(body, data);
  }

  private static String[]FIX_KEYWORDS = new String[]{
      "Category",             "Category",
      "Address",              "Address",
      "Precint",              "Precinct",
      "Sector",               "Sector",
      "Geo",                  "GEO",
      "ESZ",                  "ESZ",
      "Ward",                 "Ward",
      "Intersection",         "Intersection",
      "Opened Date / Time",   "Date / Time\nOpen"
  };

  private String fixit(String body) {
    if (!body.startsWith("Event Number:")) return body;

    StringBuilder sb = new StringBuilder("Event No:");
    int lastPt = 13;
    boolean address = false;
    for (int keyNdx = 0; keyNdx < FIX_KEYWORDS.length; keyNdx+=2) {
      String keyword = FIX_KEYWORDS[keyNdx];
      String replace = FIX_KEYWORDS[keyNdx+1];
      int pt = body.indexOf(keyword, lastPt);
      if (pt >= 0) {
        String segment = body.substring(lastPt, pt);
        if (address) segment = segment.replace("   ", ",");
        sb.append(segment);
        if  (body.charAt(pt-1) != '\n') sb.append('\n');
        sb.append(replace
            );
        lastPt = pt + keyword.length();
        if (lastPt <= body.length() && body.charAt(lastPt) != ':') sb.append(':');
      }
      address = keyword.equals("Address");
    }
    sb.append(body.substring(lastPt));
    return sb.toString();
  }

}
