package net.anei.cadpage.parsers.WV;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WVWoodCountyParser extends FieldProgramParser {

  public WVWoodCountyParser() {
    super("WOOD COUNTY", "WV",
          "CFS_Address:ADDRCITYST! Incident_Code:CALL! Additional_Incident_Codes:CALL2! Call_Details:INFO!");
  }

  @Override
  public String getFilter() {
    return "ctc@wchsem.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
