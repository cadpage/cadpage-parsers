package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class DispatchA87Parser extends FieldProgramParser {

  public DispatchA87Parser(String defCity, String defState) {
    super(defCity, defState,
          "Call_Time:DATETIME Police_Call_Type:CALL! Fire_Call_Type:CALL/L! EMS_Call_Type:CALL/L? Address:ADDRCITY! Common_Name:PLACE! Closest_Intersection:X! " +
              "Additional_Location_Info:PLACE! Nature_of_Call:CALL/SDS! Assigned_Units:UNIT! ( Police_Priority:PRI! Police_Status:SKIP! Fire_Status:SKIP! | ) " +
              "Quadrant:MAP! District:MAP/L! Beat:MAP/L! CFS_Number:SKIP? Primary_Incident:ID! Narrative:INFO! INFO/N+ Latitude:GPS END");
  }

  private static final Pattern DELIM = Pattern.compile("\n| (?=(?:Common Name|Additional Location Info|Police Status|Fire Status|District|Beat):)");
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.contains(field)) return;
      super.parse(field, data);
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" Longitude: ", ",");
      super.parse(field, data);
    }
  }
}
