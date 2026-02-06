package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA11Parser;

/**
 * Sussex County, NJ
 */
public class NJSussexCountyBParser extends DispatchA11Parser {


  public NJSussexCountyBParser() {
    super(CITY_CODES, "SUSSEX COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "paging@sussexcountysheriff.com,sussexco911@gmail.com,911mail@sussexcountysheriff.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, ".");
    int pt = data.strCity.indexOf('/');
    if (pt >= 0) {
      data.strState = data.strCity.substring(pt+1);
      data.strCity = data.strCity.substring(0,pt);
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AY",  "ALLAMUCHY TWP",
      "COL", "COLUMBIA",
      "DF",  "DINGMANS FERRY/PA",
      "DT",  "MILFORD/PA",
      "DV",  "DOVER",
      "JEF", "JEFFERSON",
      "NE",  "NETCONG",
      "ROX", "ROXBURY TWP"
  });

}
