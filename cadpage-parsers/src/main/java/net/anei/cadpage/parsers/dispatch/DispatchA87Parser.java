package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class DispatchA87Parser extends FieldProgramParser {

  public DispatchA87Parser(String defCity, String defState) {
    super(defCity, defState,
          "Call_Time:DATETIME Police_Call_Type:CALL! Fire_Call_Type:CALL/L! EMS_Call_Type:CALL/L? Address:ADDRCITYST/S6! Common_Name:PLACE! Primary_Caller_Phone_Number:PHONE? Latitude,Longitude:GPS? Closest_Intersection:X! Additional_Location_Info:PLACE! " +
              "( Quadrant:MAP! Beat:MAP/L! Assigned_Units:UNIT! CFS_Number:SKIP? Incident:ID! Police_Radio_Channel:CH! Fire_Radio_Channel:CH/L! EMS_RadioChannel:CH/L Narrative:INFO/N! INFO/N+ " +
              "| Nature_of_Call:CALL/SDS? Assigned_Units:UNIT! ( Police_Priority:PRI! Police_Status:SKIP! Fire_Priority:PRI? Fire_Status:SKIP! EMS_Priority:PRI? EMS_Status:SKIP? | ) " +
                    "( Quadrant:MAP! District:MAP/L! Beat:MAP/L! | ) CFS_Number:SKIP? Primary_Incident:ID? Narrative:INFO! INFO/N+ Latitude:GPS END " +
              ")");
  }

  private static final Pattern DELIM = Pattern.compile("\n| (?=(?:Common Name|Additional Location Info|Police Status|Fire Status|EMS Status|District|Beat|(?:Police|Fire|EMS) Radio Channel):)");
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("Latitude, Longitude:", "Latitude,Longitude:");
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("ADDRCITYST")) return new BaseAddressCityStateField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("PRI")) return new BasePriorityField();
    if (name.equals("GPS")) return new BaseGPSField();
    return super.getField(name);
  }

  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.contains(field)) return;
      super.parse(field, data);
    }
  }

  private class BaseAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private class BasePriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty() || data.strPriority.contains(field)) return;
      data.strPriority = append(data.strPriority, "/", field);
    }
  }

  private class BaseGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" Longitude: ", ",");
      super.parse(field, data);
    }
  }
}
