package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class LAStTammanyParishAParser extends FieldProgramParser {

  public LAStTammanyParishAParser() {
    this("ST TAMMANY PARISH", "LA");
  }

  LAStTammanyParishAParser(String defCity, String defState) {
    super(defCity, defState,
          "Event_Date:DATETIME_UNIT_ID! Address:ADDR_X_CITY! GPS?  Event_Type:CODE_CALL! REPORT! Remarks:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "stfpd1@stfpd1.dapage.net,alerts@pssalerts.info,alerts@tangi911alerts.net";
  }

  @Override
  public String getAliasCode() {
    return "LAStTammanyParishA";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("\\bDistrict[ #]+(\\d+)\\b");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.find()) data.strSource = match.group(1);

    return parseFields(body.split("\n+"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME_UNIT_ID")) return new MyDateTimeUnitIdField();
    if (name.equals("ADDR_X_CITY")) return new MyAddressCrossCityField();
    if (name.equals("GPS")) return new GPSField("Lat: .* Long: .*", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("REPORT")) return new IdField("Report# *(\\S*)", true);
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_UNIT_ID_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d) +Unit# (\\S+)  CAD# (.*)\\.");
  private class MyDateTimeUnitIdField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_UNIT_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strUnit = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME UNIT";
    }
  }

  private static final Pattern ADDR_X_CITY_PTN = Pattern.compile("(.*?) +(?:# *(.*?) +)?Intersection:(.*?)(?: +Jurisdiction: *(.*))?");
  private class MyAddressCrossCityField extends Field {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ".");
      Matcher match = ADDR_X_CITY_PTN.matcher(field);
      if (!match.matches()) abort();
      parseAddress(match.group(1).trim(), data);
      data.strApt = append(data.strApt, "-", getOptGroup(match.group(2)));
      data.strCross = stripFieldStart(match.group(3).trim(), "0 ");
      data.strCity = getOptGroup(match.group(4));
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT X CITY";
    }
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("(")) {
        int pt = field.indexOf(')', 1);
        if (pt >= 0) {
          data.strCode = field.substring(1, pt).trim();
          field = field.substring(pt+1).trim();
        }
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
