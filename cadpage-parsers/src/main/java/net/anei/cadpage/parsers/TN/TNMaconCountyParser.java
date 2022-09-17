package net.anei.cadpage.parsers.TN;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNMaconCountyParser extends FieldProgramParser {

  public TNMaconCountyParser() {
    super(CITY_LIST, "MACON COUNTY", "TN",
          "CALL ( ADDRCITY! | ADDR! CITY? ) X? INFO/N+");
    setupCities(MISSPELLED_CITIES);
  }

  @Override
  public String getFilter() {
    return "macondispatch@macontn911.com";
  }

  private static final Pattern DELIM = Pattern.compile("[;\n]");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldEnd(body, ",");

    if (!parseFields(DELIM.split(body), data)) return false;
    String fixCity = MISSPELLED_CITIES.getProperty(data.strCity.toUpperCase());
    if (fixCity != null) data.strCity = fixCity;
    return !data.strCity.isEmpty() || isValidAddress(data.strAddress);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      String info = null;
      int pt = field.indexOf(':');
      if (pt >= 0) {
        info = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      if (!super.checkParse(field, data)) return false;
      if (info != null) data.strSupp = info;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CITY INFO?";
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "LAFAYETTE",
      "RED BOILING SPRINGS",

      // Unincorporated communities
      "BEECH BOTTOM",
      "BEECH HILL",
      "HILLSDALE",
      "WILLETTE",

      // Summer County
      "WESTMORELAND",

      // Trousdale County
      "HARTSVILLE"
  };

  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[] {
      "LAFYATTE",             "LAFAYETTE",
      "RED BOILING SPRING",   "RED BOILING SPRINGS",
  });

}
