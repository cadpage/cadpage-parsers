package net.anei.cadpage.parsers.MN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNBeckerCountyParser extends FieldProgramParser {

  public MNBeckerCountyParser() {
    super("BECKER COUNTY", "MN",
          "CFS_Location:ADDRCITYST! Incident_Code:CODE! Responder_Units:UNIT! Call_Details:INFO! CFS_Number:ID! Call_Time:DATETIME! END");
    setupProtectedNames("TOWN AND COUNTRY");
  }

  @Override
  public String getFilter() {
    return "BeckerCountyZuercher@co.becker.mn.us";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d");
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
