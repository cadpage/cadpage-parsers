package net.anei.cadpage.parsers.SD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SDMinnehahaCountyBParser extends FieldProgramParser {

  public SDMinnehahaCountyBParser() {
    super("MINNEHAHA COUNTY", "SD",
          "ADDRCITY MAP CALL CALL/SDS? ID PLACE PLACE X X! UNIT! LATITUDE GPS1 LONGITUDE GPS2 END");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "ZuercherNoReply@siouxfalls.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("; Latitude:", ": Latitude:");
    if (body.endsWith(":")) body += ' ';
    return parseFields(body.split(": ", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new IdField("CFS\\d{2}-\\d{6}", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X"))  return new MyCrossField();
    if (name.equals("LATITUDE")) return new SkipField("Latitude", true);
    if (name.equals("LONGITUDE")) return new SkipField("Longitude", true);
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern ADDR_ZIP_ST_PTN = Pattern.compile("(.*), ([A-Z]{2})(?: (\\d{5}))?-?");
  private static final Pattern ADDR_GPS_PTN = Pattern.compile("\\+\\d{2,3}\\.\\d{6}, *-\\d{2,3}\\.\\d{6}");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains(",") && !field.contains("/")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ZIP_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
        zip = match.group(3);
      }
      if (ADDR_GPS_PTN.matcher(field).matches()) {
        data.strAddress = field;
      } else {
        super.parse(field, data);
      }
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "I 29 MM 84",                              "+43.614354,-96.770602",
      "I 29 MM 85",                              "+43.630634,-96.770470",
      "I 29 MM 86",                              "+43.644865,-96.771152",
      "I 90 MM 396",                             "+43.612866,-96.763988",
      "I 90 MM 397",                             "+43.611506,-96.751588",
      "I 90 MM 398",                             "+43.607213,-96.728260",
      "I 90 MM 399",                             "+43.605563,-96.712834",
      "I 90 MM 400",                             "+43.606675,-96.691759",
      "I 90 MM 401",                             "+43.607791,-96.670875",
      "I 90 MM 402",                             "+43.607910,-96.655458"
  });
}
