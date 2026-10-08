package net.anei.cadpage.parsers.SD;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SDLakeCountyParser extends FieldProgramParser {

  public  SDLakeCountyParser() {
    super("LAKE COUNTY", "SD",
          "( Cancel_and_Stand_Down_for_Call_Type:CALL_ADDRCITYST! CAD#:ID! " +
          "| You_are_needed_at_Location:ADDRCITYST! GPS:GPS_X! Call_Type:CALL! CAD#:ID! " +
          ") Details:INFO!");
  }

  @Override
  public String getFilter() {
    return "zuercher@lake.sdcounty.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Information") && !subject.equals("Cancel Page")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL_ADDRCITYST")) return new MyCallAddressCityStateField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("GPS_X")) return new MyGPSCrossField();
    return super.getField(name);
  }

  private class MyCallAddressCityStateField extends MyAddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" at *");
      if (pt < 0) abort();
      data.strCall = append("Cancel", " ", field.substring(0,pt).trim());
      field = field.substring(pt+4);
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CALL " + super.getFieldNames();
    }
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("*")) abort();
      int pt = field.indexOf("*", 1);
      if (pt < 0) abort();
      super.parse(field.substring(1, pt).trim(), data);
      data.strPlace = field.substring(pt+1).trim();
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyGPSCrossField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" Nearest Intersection");
      if (pt < 0) abort();
      setGPSLoc(field.substring(0,pt), data);
      data.strCross = field.substring(pt+21).trim();
    }

    @Override
    public String getFieldNames() {
      return "GPS X";
    }
  }
}
