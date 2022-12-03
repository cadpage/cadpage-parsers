package net.anei.cadpage.parsers.IN;


import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/*
 * Vanderburgh County, IN
 */
public class INVanderburghCountyParser extends DispatchOSSIParser {

  public INVanderburghCountyParser() {
    super(CITY_CODES, "VANDERBURGH COUNTY", "IN",
          "( CANCEL ADDR? " +
          "| FYI? BOX UNIT? CALL ( ADDR/Z CH! | PLACE ADDR/Z CH! | PLACE? ADDR! ) CH? " +
          ") INFO/N+");
    removeWords("TERRACE");
  }

  @Override
  public String getFilter() {
    return "CAD@vanderburghsheriff.com,CAD@vanderburghsheriff.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("[,A-Z0-9]+", true);
    if (name.equals("CH")) return new ChannelField("TAC ?\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Radio Channel:")) {
        field = field.substring(14).trim();
        if (data.strChannel.length() == 0) data.strChannel = field;
        return;
      }
      else if ("Radio Channel:".startsWith(field)) return;
      String city = CITY_CODES.getProperty(field);
      if (city != null) {
        data.strCity = city;
        return;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH? " + super.getFieldNames() + " CITY";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "EV", "EVANSVILLE"
  });
}
