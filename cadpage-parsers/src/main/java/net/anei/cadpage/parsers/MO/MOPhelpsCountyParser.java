package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOPhelpsCountyParser extends FieldProgramParser {

  public MOPhelpsCountyParser() {
    this("PHELPS COUNTY", "MO");
  }

  MOPhelpsCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "CFS:ID! Incident_Code:CALL! Address:ADDRCITY! Closest_Intersection:X? Lat:GPS1! Long:GPS2! Units:UNIT! Narrative:INFO/N! END");
  }

  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }

  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" Cross streets:", " Closest Intersection:");
    body = body.replace(",Long:", " Long:");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(":")) {
        data.strCall = "ALERT";
      } else {
        int pt = field.indexOf(':');
        if (pt > 0) {
          data.strCode = field.substring(0,pt).trim();
          field = field.substring(pt+1).trim();
        }
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = p.getLastOptional(';');
      String city = p.getLastOptional(',');
      Matcher match = ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        String zip = match.group(2);
        city = p.getLastOptional(',');
        if (city.length() == 0 && zip != null) city = zip;
      }
      super.parse(p.get(), data);
      data.strCity = city;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST PLACE";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("(?:^|; *)\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      for (String part : INFO_BRK_PTN.split(field)) {
        super.parse(part.trim(), data);
      }
    }
  }

  private static final Pattern PVTNNNN_PTN = Pattern.compile("\\b(?:PVT|PRIVATE DR) *(\\d+)\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String address) {
    return PVTNNNN_PTN.matcher(address).replaceAll("PVT DRIVE $1");
  }

  // Google has problems mapping STATE BB.  This seems to be limited to this one highway

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return sAddress.replace("STATE BB", "STATE HWY BB");
  }
}