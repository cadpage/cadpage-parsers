package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MOCaldwellCountyBParser extends FieldProgramParser {

  public MOCaldwellCountyBParser() {
    super("CALDWELL COUNTY", "MO",
          "Incident_Code:CALL! CFS_Location:ADDRCITYST! Incident_Codes_Description:CALL/SDS! Call_Details:INFO! Call_Time:DATETIME! " +
              "CFS_Location_Entered:SKIP! CFS_Location_Details:PLACE! CFS_Location_Address:SKIP! CFS_Common_Address:PLACE! CFS_Number:ID! " +
              "CFS_Response_Times:TIMES CFS_Assigned:SKIP! Command_Log:SKIP! Incident_Code:SKIP! Incident_Modifier:SKIP! Initial_Reporter:NAME! " +
              "Initial_Reporter_Location:SKIP! Initial_Reporter_Contact_Number:PHONE! Initial_Reporter_From_Number_(ANI/ALI):SKIP! " +
              "Location_Notes:EMPTY! Cross_Streets:X! Nearest_Intersection:SKIP! Responder_Agencies:SRC! Responder_Units:UNIT! " +
              "Unit_Response_Times:SKIP! Use_Caution:ALERT! END");
  }

  @Override
  public String getFilter() {
    return "centralsquare@cameronmo.com";
  }

  String times;

  @Override
  protected boolean parseMsg(String body, Data data) {
    times = "";
    try {
      if (!super.parseMsg(body, data)) return false;
      if (data.msgType == MsgType.RUN_REPORT) {
        data.strSupp = append(times, "\n", data.strSupp);
      }
      return true;
    } finally {
      times = null;
    }
  }

  @Override
  protected boolean parseFields(String[] fields, Data data) {
    for (int jj = 0; jj<fields.length; jj++) {
      fields[jj] = cleanField(fields[jj]);
    }
    return super.parseFields(fields, data);
  }

  private String cleanField(String fld) {
    if (fld.endsWith(": None")) {
      fld = fld.substring(0,fld.length()-5);
    }
    return fld;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new DateTimeField("(\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d)(?: - .*)?", true);
    if (name.equals("TIMES")) return new MyTimesField();
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ALERT")) return new MyAlertField();
    return super.getField(name);
  }

  private static final Pattern ADDR_PLACE_PTN = Pattern.compile("(.* \\d{5}) +(.*)");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PLACE_PTN.matcher(field);
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

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private static final Pattern TIMES_BRK_PTN = Pattern.compile(" *; *");
  private static final Pattern TIMES_PTN = Pattern.compile("(.*?): +.*");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String time : TIMES_BRK_PTN.split(field)) {
        Matcher match = TIMES_PTN.matcher(field);
        if (match.matches()) {
          times = append(times, "\n", time);
          if (match.group(1).equals("Completed")) data.msgType = MsgType.RUN_REPORT;
        }
      }
    }
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field.replace("; ", ","), data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field.replace("; ", ","), data);
    }
  }

  private class MyAlertField extends AlertField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Yes")) data.strAlert = "USE CAUTION!!!";
    }
  }
}
