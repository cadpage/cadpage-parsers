package net.anei.cadpage.parsers.MO;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchBCParser;


public class MOScottCountyAParser extends DispatchBCParser {


  public MOScottCountyAParser() {
    super("SCOTT COUNTY", "MO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "SCOTTCITY@PUBLICSAFETYSOFTWARE.NET,SCOTTCITY@itiusa.com,@OMNIGO.COM,chaffeefd@omnigo.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;

    // They do weird things with the cross street information :(
    // But only the old A33 version, go figure
    if (!body.startsWith("<html>")) {
      data.strCross = stripFieldEnd(data.strCross, "CITY FIRE");
      data.strCross = stripFieldEnd(data.strCross, "FIRE");
      if (!data.strCross.isEmpty() && !data.strCross.contains("/")) {
        int pt = data.strCross.indexOf(',');
        if (pt >= 0) {
          data.strState = data.strCross.substring(pt+1).trim();
          data.strCity = append(data.strCity, " ", data.strCross.substring(0,pt).trim());
          data.strCross = "";
        } else if (data.strState.length() == 0) {
          if (data.strCross.equals("MO")) {
            data.strState = data.strCross;
            data.strCross = "";
          } else if (!isValidAddress(data.strCross) && !Character.isDigit(data.strCross.charAt(0)) &&
                     !data.strCross.startsWith("RAMP")) {
            data.strCity = append(data.strCity, " ", data.strCross);
            data.strCross = "";
          }
        }
      }
    }
    return true;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "416 COUNTY HIGHWAY 212",               "+37.199800,-89.570200"
  });
}