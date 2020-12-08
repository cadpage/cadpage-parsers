package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHShelbyCountyBParser extends FieldProgramParser {

  public OHShelbyCountyBParser() {
    super("SHELBY COUNTY", "OH",
          "CALL CALL/L+? ( ID INFO/L+? GPS | GPS ) ADDR1? ADDRCITYST INFO/L+? ( ID DATETIME/d! | DATETIME/d! ) END");
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static Pattern DATE_PTN = Pattern.compile("\\b(\\d\\d)/(\\d\\d)/(\\d\\d)\\b");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.isEmpty()) body = subject + '/' + body;

    body = body.replace("maps.google.com/", "maps.google.com");
    body = DATE_PTN.matcher(body).replaceFirst("$1-$2-$3");
    return parseFields(body.split("/"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("CFS\\d{3,}", true);
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("GPS")) return new GPSField("maps\\.google\\.com\\?q=(.*)", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddress1Field extends AddressField {
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
