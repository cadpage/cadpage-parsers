package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;

/*
Grover Beach, CA
 */

public class CAGroverBeachParser extends DispatchA22Parser {

  private static final Pattern MARKER = Pattern.compile("(?:UNIT DISPATCH|INCIDENT NEW)\n");

  public CAGroverBeachParser() {
    super(CITY_CODES, "SAN LUIS OBISPO COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "Dispatch@GBPD.ORG";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end()).trim();
    if (!super.parseMsg(body, data)) return false;
    String city = CITY_CODES.getProperty(data.strPlace);
    if (city != null) {
      data.strCity = city;
      data.strPlace = "";
    }
    return true;
  }

  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "GB",             "GROVER BEACH",
      "OC",             "OCEANO",
      "PB",             "PISMO BEACH",
      "GROVER BEA",     "GROVER BEACH",
      "SLO CO",         "",
      "SLO_CO",         ""
  });
}
