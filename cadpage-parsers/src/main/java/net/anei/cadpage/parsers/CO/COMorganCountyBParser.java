package net.anei.cadpage.parsers.CO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class COMorganCountyBParser extends FieldProgramParser {

  public COMorganCountyBParser() {
    super("MORGAN COUNTY", "CO",
          "Incident_Code:CALL! Address:ADDRCITYST! Address_Name:PLACE! Location_Details:INFO! Dispatch's_CFS_Number:ID? Caller:NAME! Caller_Phone_Number:PHONE? How:SKIP? " +
              "Call_Time:DATETIME! Call_Location:SKIP! Responding_Units:UNIT! Call_Notes:INFO! CFS_Latitude:GPS1! CFS_Longitude:GPS2! Location_Alert:ALERT! " +
              "ProQA_Summary:INFO/N! Responder_Agencies:SRC! Unit_Response_Times:TIMES! Agency_Response_Number:SKIP 911_Information:SKIP ");
  }

  @Override
  public String getFilter() {
    return "centralsquaremorgancounty@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("ACTIVE911")) {
      if (!subject.startsWith("Response times for")) return false;
      data.msgType = MsgType.RUN_REPORT;
    }
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      super.parse(field, data);
    }
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern TIMES_BRK_PTN = Pattern.compile(" *; *");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (data.msgType != MsgType.RUN_REPORT) return;
      field = TIMES_BRK_PTN.matcher(field).replaceAll("\n");
      data.strSupp = append(field, "\n", data.strSupp);
    }
  }
}
