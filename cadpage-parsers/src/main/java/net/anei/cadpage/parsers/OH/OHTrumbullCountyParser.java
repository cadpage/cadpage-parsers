package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Trumble County, OH
 */
public class OHTrumbullCountyParser extends FieldProgramParser {

  public OHTrumbullCountyParser() {
    super(CITY_LIST, "TRUMBULL COUNTY", "OH",
          "ADDRCITYST/S PLACE? CALL! UNIT X! INFO+? GPS1 GPS2 END");
  }

  @Override
  public String getFilter() {
    return "911no@co.trumbull.oh.us";
  }

  @Override
  public int  getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile("\\*{2,}");

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("*")) return false;
    body = body.substring(1).trim();
    return parseFields(DELIM.split(body), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("/")) return false;

      field = field.replace("//", "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      String place = "";
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt < 0) abort();
        place = field.substring(pt+1, field.length()-1).trim();
        field = field.substring(0,pt).trim();
      }
      field = field.replace('@', '&');
      super.parse(field, data);
      data.strPlace = append(data.strPlace, " - ", place);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }

  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{8,}");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type, GPS_PTN, true);
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CORTLAND",
      "GIRARD",
      "HUBBARD",
      "NILES",
      "WARREN",
      "YOUNGSTOWN",
      "VILLAGES",
      "LORDSTOWN",
      "MCDONALD",
      "NEWTON FALLS",
      "ORANGEVILLE",
      "WEST FARMINGTON",
      "YANKEE LAKE",

      // Townships
      "BAZETTA",
      "BLOOMFIELD",
      "BRACEVILLE",
      "BRISTOL",
      "BROOKFIELD",
      "CHAMPION",
      "FARMINGTON",
      "FOWLER",
      "GREENE",
      "GUSTAVUS",
      "HARTFORD",
      "HOWLAND",
      "HUBBARD",
      "JOHNSTON",
      "KINSMAN",
      "LIBERTY",
      "MECCA",
      "MESOPOTAMIA",
      "NEWTON",
      "SOUTHINGTON",
      "VERNON",
      "VIENNA",
      "WARREN",
      "WEATHERSFIELD",

      // Unincorporated communities
      "BRISTOLVILLE",
      "BURGHILL",
      "CENTER OF THE WORLD",
      "FARMDALE",
      "FOWLER",
      "HARTFORD",
      "NORTH BLOOMFIELD",
      "SOUTHINGTON"
  };
}
