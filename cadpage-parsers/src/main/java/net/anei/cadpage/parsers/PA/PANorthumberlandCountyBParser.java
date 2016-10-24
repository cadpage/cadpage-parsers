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
    return "noreply@norrycopa.net";
  }

  @Override
  protected boolean parseMsg(String body, MsgInfo.Data data) {
    int pt = body.indexOf("<div style=");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseFields(body.split("~"), data);
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("POINT TWP")) city += ",NORTHUMBERLAND COUNTY";
    return city;
  }
}
