package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VARockbridgeCountyCParser extends FieldProgramParser {

  public VARockbridgeCountyCParser() {
    super("ROCKBRIDGE COUNTY", "VA",
          "DATETIME! CFS_Number:SKIP! Nature_of_Call:CALL! Address:ADDRCITY! Narrative:INFO! INFO/N+ Additional_Location_Info:PLACE! " +
              "Assigned_Units:UNIT! Radio_Channel:CH! Incident_#:ID! Closest_Intersection:X! Call_Type:CALL/SDS! END");
  }

  @Override
  public String getFilter() {
    return "dispatch@lexingtonva.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(':');
      if (pt >= 0) {
        data.strPlace = stripFieldStart(field.substring(pt+1).trim(), "-");
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

}
