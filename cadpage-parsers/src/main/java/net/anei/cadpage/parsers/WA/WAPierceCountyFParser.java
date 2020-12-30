package net.anei.cadpage.parsers.WA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WAPierceCountyFParser extends FieldProgramParser {

  public WAPierceCountyFParser() {
    super(WAPierceCountyEParser.CITY_CODES, "PIERCE COUNTY", "WA",
          "CallDescription:CALL! CallType:CALL/SDS! CallAddress:ADDR! Municipality:CITY! District:MAP FirstUnitDispatchedTS:DATETIME! Units:UNIT! IncidentNumber:ID! Comments:INFO! Latitude:GPS1! Longitude:GPS2! END");
  }

  @Override
  public String getFilter() {
    return "do-not_reply@southsound911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("([A-Z]+) - (\\S+)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    return parseFields(body.split("; "), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern COMMA_PTN = Pattern.compile("\\s*,\\s+");

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = COMMA_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_WPH_PTN = Pattern.compile("WPH[1-2]");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = COMMA_PTN.matcher(field).replaceAll("\n").trim();
      field = stripFieldEnd(field, ",");
      if (INFO_WPH_PTN.matcher(field).lookingAt()) {
        int pt = field.indexOf('\n');
        if (pt < 0) return;
        field = field.substring(pt+1).trim();
      }
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return WAPierceCountyParser.adjustMapAddressCommon(sAddress);
  }
}
