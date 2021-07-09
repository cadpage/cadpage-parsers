package net.anei.cadpage.parsers.WV;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WVBooneCountyBParser extends FieldProgramParser {

  public WVBooneCountyBParser() {
    super("BOONE COUNTY", "WV",
          "Location:ADDRCITYST! Cross_Street:X! CFS_#:ID! 911_Lat:GPS1! 911_Long:GPS2! Details:INFO! END");
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com,boonezuercher@gmail.com";
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
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
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
