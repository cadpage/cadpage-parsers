package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHWarrenCountyEParser extends FieldProgramParser {
  
  public OHWarrenCountyEParser() {
    super("WARREN COUNTY", "OH", 
          "( SELECT/RR UNITS_RESPONDED_TO_CFS_ON:DATETIME! INCIDENT_CODE:CALL! ADDITIONAL_INCIDENT_CODE(s):SKIP! DISPOSITION:SKIP! " +
                      "CFS_LOCATION:ADDR! CROSS_STREETS:X! CFS_DETAILS:INFO! UNITS_ASSIGNED_TO_CFS:UNIT! FIRE_DEPARTMENT_CFS_NUMBER:ID! " +
          "| ADDR PLACE X INFO+? UNIT ID! EMPTY END )");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com,zuercher@lebanonohio.gov";
  }
  
  private static final Pattern DELIM = Pattern.compile("(?<!\\d):|:(?!\\d)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() == 0) return false;
    if (subject.startsWith("CFS - Completed - #")) {
      data.msgType = MsgInfo.MsgType.RUN_REPORT;
      setSelectValue("RR");
      return parseFields(body.split("\n+"), data);
    } else {
      data.strCall = subject;
      setSelectValue("");
      int pt = body.indexOf("\n\n");
      if (pt >= 0) body = body.substring(0,pt).trim();
      return parseFields(DELIM.split(body, -1), data);
    }
  }
  
  @Override
  public String getProgram() {
    return "CALL? " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new IdField("[A-Z]{3}\\d{8}", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("^\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String connect = ": ";
      for (String part : field.split("; ")) {
        part = part.trim();
        part = INFO_DATE_TIME_PTN.matcher(part).replaceFirst("");
        data.strSupp = append(data.strSupp, connect, part);
        connect = "\n";
      }
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", " ");
      super.parse(field, data);
    }
  }
}
