package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class OHPickawayCountyBParser extends FieldProgramParser {

  public OHPickawayCountyBParser() {
    super("PICKAWAY COUNTY", "OH",
          "( INFO1 | ADDRCITYST/SP Call_Details:INFO2! Responder_Units:UNIT! ) END");
  }

  @Override
  public String getFilter() {
    return "zuercher@pickawaysheriff.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("([A-Z]{2,4}\\d{2}-\\d{5}) - ([A-Z-0-9]*) *: *(.*)");
  private static final Pattern CALL_TRAIL_JUNK_PTN = Pattern.compile("(?: *[/:] None)*(?: Update)?$");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher  match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strCode = match.group(2);
    String call = match.group(3);
    data.strCall = CALL_TRAIL_JUNK_PTN.matcher(call).replaceFirst("");

    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "ID CODE CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO1")) return new InfoField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d +(.*)", true);
    if (name.equals("INFO2")) return new MyInfo2Field();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("http://maps.apple.com/\\?ll=(.*)");
  private class MyAddressCityStateField extends AddressCityStateField {

    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        String place = field.substring(pt+1, field.length()-1).trim();
        Matcher match = GPS_PTN.matcher(place);
        if (match.matches()) {
          setGPSLoc(match.group(1), data);
        } else {
          data.strPlace = append(data.strPlace, " - ", place);
        }
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE GPS";
    }
  }

  private static final Pattern INFO2_BRK_PTN = Pattern.compile("[ ;]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");

  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO2_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private static final Pattern UNIT_BRK_PTN = Pattern.compile("; *");

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_BRK_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }
}
