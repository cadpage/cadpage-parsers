package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHPutnamCountyParser extends FieldProgramParser {

  public OHPutnamCountyParser() {
    super("PUTNAM COUNTY", "OH",
          "Location_Address:ADDRCITYST_GPS! Location_Name:PLACE! Important_Details:INFO! Units:UNIT! Call_For_Service:ID! Nearest_Intersection:X! Phone:PHONE! END");
  }

  private static final Pattern MASTER = Pattern.compile("(.*) Call For Service #(\\S+) Please respond immediately. (.*)");
  private static final Pattern MISSING_COLON_PTN = Pattern.compile("(?<=Location Address)(?!:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Call Type:")) return false;
    data.strCall = subject.substring(10).trim();
    body = body.replace('\n', ' ');
    body = MISSING_COLON_PTN.matcher(body).replaceAll(":");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    body = match.group(1) + " Call For Service:" + match.group(2)+ ' ' + match.group(3);
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST_GPS")) return new MyAddressCityStateGPSField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern ADDR_GPS_PTN = Pattern.compile("(.*) ([-+]?\\d{2}\\.\\d{6,}) *([-+]?\\d{2}\\.\\d{6,})");
  private class MyAddressCityStateGPSField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_GPS_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        setGPSLoc(match.group(2)+','+match.group(3), data);
      } else {
        field = stripFieldEnd(field, " NoneNone");
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS";
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }
}
