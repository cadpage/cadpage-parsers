package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;

/*
Paso Robles, CA
 */

public class CAPasoRoblesParser extends DispatchA22Parser {
  
  public CAPasoRoblesParser() {
    super(CITY_CODES, "PASO ROBLES", "CA");
  }
  
  @Override
  public String getFilter() {
    return "Cad@prcity.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return true;
    if (data.strAddress.endsWith(",SLO_CO")) {
      data.strAddress = data.strAddress.substring(0,data.strAddress.length()-7).trim();
      if (data.strCity.length() == 0) data.strCity = "SAN LUIS OBISPO COUNTY";
    }
    return true;
  }

  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "PR",     "PASO ROBLES",
      "SLO_CO", "SAN LUIS OBISPO COUNTY"
  });
}
