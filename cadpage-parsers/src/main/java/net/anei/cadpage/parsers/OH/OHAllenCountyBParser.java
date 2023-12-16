package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHAllenCountyBParser extends FieldProgramParser {

  public OHAllenCountyBParser() {
    super("ALLEN COUNTY", "OH",
          "ADDR CITY? ST_ZIP? PLACE PLACE/CS+? INFO! INFO+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "paging@shawneetwpfire.com";
  }

  private static final Pattern TRAIL_COMMA_PTN = Pattern.compile("[ ,]+$");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    data.strCall = subject;

    int pt = body.indexOf(" Location Alert:");
    if (pt < 0) return false;
    data.strAlert = TRAIL_COMMA_PTN.matcher(body.substring(pt+16).trim()).replaceFirst("");
    body = body.substring(0,pt).trim();
    return parseFields(body.split(","), data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram() + " ALERT";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new CityField("[ A-Z]+", true);
    if (name.equals("ST_ZIP")) return new StateField("([A-Z]{2})(?: +\\d{5})?", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|UNIT|LOT) *(\\S+)|\\d{1,4}[A-Z]?|[A-Z]");
  private class MyPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = append(data.strPlace, ", ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +(?:LOG - +)?");

  private class MyInfoField extends InfoField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("None")) return true;
      if (data.strSupp.isEmpty()) {
        Matcher match = INFO_BRK_PTN.matcher(field);
        if (!match.lookingAt()) return false;
        field = field.substring(match.end());
      }
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, ", ", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "2277 W BREESE RD",                     "+40.686152,-84.146268"
  });
}
