package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WVBooneCountyBParser extends FieldProgramParser {

  public WVBooneCountyBParser() {
    super("BOONE COUNTY", "WV",
          "Location:ADDRCITYST! Location_Name:PLACE? ( CFS_#:ID! Responding_Agencies:SRC! Caller's_Number:PHONE! Nearest_Intersection:SKIP! Cross_Street:X! " +
                                                    "| Cross_Street:X! CFS_#:ID! " +
                                                    ") 911_Lat:GPS1! 911_Long:GPS2! Uncertainty:SKIP? Details:INFO! END");
  }

  @Override
  public String getFilter() {
    return "zuercher@boonewv.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.startsWith("Type:")) return false;
    data.strCall = subject.substring(5).trim();

    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDRESS_CITY_ST_PLACE_PTN = Pattern.compile("(.*, WV(?: +\\d{5})?) *(.*)");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDRESS_CITY_ST_PLACE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strPlace = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("[; ]*\\b\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_DATE_TIME_PTN.matcher(field).replaceAll("");
      super.parse(field, data);
    }
  }
}
