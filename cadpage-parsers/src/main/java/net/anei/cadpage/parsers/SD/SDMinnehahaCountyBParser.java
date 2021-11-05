package net.anei.cadpage.parsers.SD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SDMinnehahaCountyBParser extends FieldProgramParser {

  public SDMinnehahaCountyBParser() {
    super("MINNEHAHA COUNTY", "SD",
          "ADDRCITY MAP CALL CALL/SDS? ID PLACE PLACE X X! UNIT! LATITUDE GPS1 LONGITUDE GPS2 END");
  }

  @Override
  public String getFilter() {
    return "noreply@siouxfalls.org";
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

  private static final Pattern ADDR_ZIP_ST_PTN = Pattern.compile("(.*), ([A-Z]{2})(?: (\\d{5}))");
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
}
