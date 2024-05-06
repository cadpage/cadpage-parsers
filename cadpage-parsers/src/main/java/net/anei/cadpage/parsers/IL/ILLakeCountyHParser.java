package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILLakeCountyHParser extends FieldProgramParser {

  public ILLakeCountyHParser() {
    super("LAKE COUNTY", "IL",
          "Date/Time:DATETIME! Call_Type:CALL! Description:CALL! Area:MAP! Units:UNIT! Location:ADDRCITY! Common_Name:PLACE! City:CITY! Incident_#:ID! Units:SKIP! Lat:GPS1! Long:GPS2! END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" Long:", "\nLong:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d|", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.contains(field)) return;
      if (field.contains(data.strCall)) {
        data.strCall = field;
      } else {
        data.strCall = append(data.strCall, " - ", field);
      }
    }
  }
}
