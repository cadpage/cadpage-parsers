package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHShelbyCountyBParser extends FieldProgramParser {

  public OHShelbyCountyBParser() {
    super("SHELBY COUNTY", "OH",
          "CALL CALL/L+? ( ( ID INFO/L+? GPS | GPS ) ADDR1? ADDRCITYST INFO/L+? ( ID DATETIME/d! | DATETIME/d! ) " +
                        "| ( ADDR1 ADDRCITYST | ADDRCITYST ) INFO/L+? DATETIME/d ID SRC UNIT X X! INFO/L+? GPS1 GPS2 " +
                        ") END");
  }

  @Override
  public String getFilter() {
    return "noreply@shelbycountysheriff.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static Pattern DATE_PTN = Pattern.compile("/(\\d\\d)/(\\d\\d)/(\\d\\d) ");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    body = body.replace("maps.google.com/", "maps.google.com");
    body = DATE_PTN.matcher(body).replaceFirst("/$1-$2-$3 ");
    return parseFields(body.split("/"), data);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d\\d.\\d{6,}");

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new IdField("CFS\\d{3,}", true);
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("GPS")) return new GPSField("maps\\.google\\.com\\?q=(.*)", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("GPS1")) return new GPSField(1, GPS_PTN, true);
    if (name.equals("GPS2")) return new GPSField(2, GPS_PTN, true);
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MyAddress1Field extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("None")) return false;
      if (field.contains(",")) return false;
      if (checkAddress(field) !=  STATUS_STREET_NAME) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      data.strAddress = field;
    }
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, " & ", field);
      data.strAddress = "";
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_X_PTN = Pattern.compile(".*\\) and .*\\)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;

      if (data.strCross.isEmpty() && INFO_X_PTN.matcher(field).matches()) {
        data.strCross = field;
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }
}
