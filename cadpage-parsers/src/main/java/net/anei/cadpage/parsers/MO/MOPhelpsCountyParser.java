package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOPhelpsCountyParser extends FieldProgramParser {

  public MOPhelpsCountyParser() {
    this("PHELPS COUNTY", "MO");
  }

  MOPhelpsCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "CFS:ID! Incident_Code:CALL! Address:ADDRCITYST! Closest_Intersection:X? Lat:GPS1! Long:GPS2! Units:UNIT! Narrative:INFO/N! END");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  public String getFilter() {
    return "no-reply@zuercherportal.com,dispatch@rollacity.org";
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
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
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

  private static final Pattern PERIOD_PTN = Pattern.compile("\\. *");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(';');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
      data.strCity = PERIOD_PTN.matcher(data.strCity).replaceAll(" ");
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
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

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "15650 COUNTY ROAD 2430",             "+38.032489, -91.682193"
  });
}