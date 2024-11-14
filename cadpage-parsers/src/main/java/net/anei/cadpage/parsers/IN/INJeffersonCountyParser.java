package net.anei.cadpage.parsers.IN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class INJeffersonCountyParser extends FieldProgramParser {

  public INJeffersonCountyParser() {
    super("JEFFERSON COUNTY", "IN",
          "CFS_location:ADDRCITYST! Nature:CALL! Details:INFO! END");
  }

  @Override
  public String getFilter() {
    return "no-reply-zuercher@jeffersoncounty.in.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.endsWith(" Please respond immediately.")) return false;
    body = body.substring(0, body.length()-28).trim();
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
