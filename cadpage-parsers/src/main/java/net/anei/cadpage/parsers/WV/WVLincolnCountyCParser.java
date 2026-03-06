package net.anei.cadpage.parsers.WV;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WVLincolnCountyCParser extends FieldProgramParser {

  public WVLincolnCountyCParser() {
    super("LINCOLN COUNTY", "WV",
          "Address:SKIP! Address_Name:PLACE! Location_Details:PLACE/SDS! CFS_Number:ID! Incident_Type:CALL/SDS! Caller:NAME! Dispatcher:SKIP! " +
              "Call_Time:DATETIME! Call_Location:ADDRCITYST! Responding_Units:UNIT! Call_Notes:INFO! Message:ALERT! CFS_Latitude:GPS1! CFS_Longitude:GPS2! END");
  }

  @Override
  public String getFilter() {
    return "no-reply@csprosuite.centralsquarecloudgov.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strCall = subject;
    body = stripFieldEnd(body, " Please Respond Immediately.");
    return parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d +\\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
