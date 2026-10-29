package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchC16Parser extends FieldProgramParser {

  public DispatchC16Parser(String defCity, String defState) {
    super(defCity, defState,
          "Address:ADDRCITYST! Address_Name:PLACE! Location_Details:PLACE? CFS_Number:ID! Incident_Type:CALL/SDS! Caller:NAME! Dispatcher:SKIP! " +
              "Call_Time:DATETIME! Call_Location:SKIP! Responding_Units:UNIT! Call_Notes:INFO! Message:INFO/N! CFS_Latitude:GPS1! CFS_Longitude:GPS2! END");
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
    return "CALL? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("NAME")) return new BaseNameField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d +\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None") ||
          field.equals(data.strCall)) return;
      super.parse(field, data);
    }
  }

  private class BaseNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);;
    }
  }
}
