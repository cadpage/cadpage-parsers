package net.anei.cadpage.parsers.AR;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class ARGarlandCountyCParser extends DispatchA19Parser {

  public ARGarlandCountyCParser() {
    super(CITY_CODES, "GARLAND COUNTY", "AR");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      return super.checkParse(stripCity(field), data);
    }
    @Override
    public void parse(String field, Data data) {
      super.parse(stripCity(field), data);
    }

    private String stripCity(String city) {
      int pt = city.indexOf('-');
      if (pt >= 0) city = city.substring(0,pt).trim();
      return city;
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "GAR", "GARLAND COUNTY",
      "SAL", "SALINE COUNTY"
  });
}
