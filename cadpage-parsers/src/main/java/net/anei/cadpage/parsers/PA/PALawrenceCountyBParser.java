package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class PALawrenceCountyBParser extends DispatchH03Parser {

  public PALawrenceCountyBParser() {
    this("LAWRENCE COUNTY", "PA");
  }

  PALawrenceCountyBParser(String defCity, String defState) {
    super(defCity, defState);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "C@leoc.net,@RCAD911.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    return true;
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("JACKSON")) return "COOPERSTOWN";
    return city;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "1560 AIRPORT RD",                      "+41.377640,-79.858340"
 });}
