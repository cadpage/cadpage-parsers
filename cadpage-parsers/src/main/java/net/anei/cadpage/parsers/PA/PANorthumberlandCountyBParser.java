package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo;
import net.anei.cadpage.parsers.dispatch.DispatchA45Parser;

import java.util.Properties;

public class PANorthumberlandCountyBParser extends FieldProgramParser {

  public PANorthumberlandCountyBParser() {
    super("NORTHUMBERLAND COUNTY", "PA",
          "ID CALL CITY ADDR! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "noreply@norrycopa.net,1027194726673";
  }

  @Override
  protected boolean parseMsg(String body, MsgInfo.Data data) {
    int pt = body.indexOf("<div style=");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!super.parseFields(body.split("~"), data)) return false;
    pt = data.strCity.indexOf('(');
    if (pt >= 0) data.strCity = data.strCity.substring(0, pt).trim();
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    data.strCity = stripFieldStart(data.strCity, "MONROE/VILLAGE OF ");
    return true;
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("POINT TWP")) city += ",NORTHUMBERLAND COUNTY";
    return city;
  }
}
