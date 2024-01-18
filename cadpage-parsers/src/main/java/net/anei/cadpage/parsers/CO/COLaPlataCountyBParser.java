package net.anei.cadpage.parsers.CO;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class COLaPlataCountyBParser extends DispatchA19Parser {

  public COLaPlataCountyBParser() {
    super("LA PLATA COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com,rapid@durangogov.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    int pt = data.strCity.indexOf('/');
    if (pt >= 0) {
      data.strPlace = append(data.strPlace, " - ", data.strCity.substring(pt+1).trim());
      data.strCity = convertCodes(data.strCity.substring(0,pt).trim(), CITY_CODES);
    } else if (data.strCity.startsWith("DGO")) {
      data.strPlace = append(data.strPlace, " - ", data.strCity.substring(3).trim());
      data.strCity = "Durango";
    }
    return true;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "DGO",  "Durango"
  });


}
