package net.anei.cadpage.parsers.IL;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;



public class ILPeoriaCountyBParser extends DispatchA49Parser {
  
  public ILPeoriaCountyBParser() {
    super(CITY_CODES, "PEORIA COUNTY", "IL");
    setupCities(CITY_LIST);
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strAddress.startsWith("MUTUAL AID")) {
      if (data.strCall.length() == 0) {
        data.strCall = data.strAddress;
      } else {
        data.strSupp = append(data.strAddress, "\n", data.strAddress);
      }
      data.strAddress = "";
      parseAddress(StartType.START_ADDR, data.strSupp, data);
      data.strSupp = getLeft();
    }
    return true;
  }

  @Override
  public String getFilter() {
    return "firepage@peoriagov.org";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BA", "BARTONVILLE",
      "BE", "BELLEVUE",
      "BR", "BRIMFIELD",
      "CH", "CHILLICOTHE",
      "CO", "PEORIA COUNTY",
      "DU", "DUNLAP",
      "EL", "ELMWOOD",
      "FC", "FULTON COUNTY",
      "GL", "GLASFORD",
      "HC", "HANNA CITY",
      "KC", "KNOX COUNTY",
      "KM", "KINGSTON MINES",
      "MA", "MAPLETON",
      "NO", "NORWOOD",
      "PA", "PEORIA",
      "PH", "PEORIA HEIGHTS",
      "PR", "PRINCEVILLE",
      "WP", "WEST PEORIA"
  });
  
  private static final String[] CITY_LIST = new String[]{
    "FULTON CO",
    "KNOX CO",
    "PEORIA CO",
  };
}
