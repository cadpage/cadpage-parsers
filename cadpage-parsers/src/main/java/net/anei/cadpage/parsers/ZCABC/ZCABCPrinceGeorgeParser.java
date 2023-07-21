package net.anei.cadpage.parsers.ZCABC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZCABCPrinceGeorgeParser extends FieldProgramParser {
  public ZCABCPrinceGeorgeParser() {
    super(CITY_LIST,  "PRINCE GEORGE", "BC",
          "Date:DATETIME! Dept:SRC Type:CALL! Address:ADDRCITY/S! Unit:APT Suite:APT 1st_Cross_Street:X 2nd_Cross_Street:X Building:PLACE Common_Place_Name:PLACE INFO/N+ Latitude:GPS1 Longitude:GPS2 Google_Maps_Link:SKIP Units_Responding:UNIT");
  }

  @Override
  public String getFilter() {
    return "donotreply@princegeorge.ca,donotreply@city.pg.bc.ca,@rdffg.bc.ca";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if(!subject.equals("CAD Incident Message") && !subject.equals("Incident Message")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTime();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+) +- +(.*)");
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher m = CODE_CALL_PTN.matcher(field);
      if (m.matches()) {
        data.strCode = m.group(1);
        field = m.group(2);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d) (\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTime extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime =match.group(4);
    }
  }

  private static final Pattern INITIAL_APT_PATTERN
    = Pattern.compile("# *(\\S+?) *- *(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = INITIAL_APT_PATTERN.matcher(field);
      if (m.matches()) {
        String apt = m.group(1);
        if (!apt.equals("0")) data.strApt = m.group(1);
        field = m.group(2);
      }
      super.parse(field.replace(", BC", ""), data);
    }

    @Override
    public String getFieldNames() {
      return append (super.getFieldNames(), " ", "PLACE");
    }
  }

  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("0")) return;
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        if (data.strCity.length() == 0) data.strCity = field.substring(pt+1).trim();
        field = field.substring(0, pt).trim();
      }
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Pre-Incident Plan:")) return;
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapCity(String city) {
    int pt = city.indexOf('/');
    if (pt >= 0) city = city.substring(0, pt).trim();
    return city;
  }

  private static final String[] CITY_LIST = {
    "QUESNEL",
    "RED BLUFF"
  };
}
