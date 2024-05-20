package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHUnionCountyBParser extends FieldProgramParser {

  public OHUnionCountyBParser() {
    super("UNION COUNTY", "OH",
          "Call_Type:CODE_CALL! Address:ADDRCITY! Lat/Lon:GPS! Common_Name:PLACE! ( Cross_Streets:X! | Closest_Intersection:X! ) Fire_Box:BOX! " +
              "Assigned_Units:UNIT! Radio_Channel:CH! Primary_Inc_#:ID! CFS_#:SKIP! Narrative:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CADPaging@unioncountyohio.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    return super.getField(name);
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt < 0) abort();
      data.strCode = field.substring(0, pt).trim();
      data.strCall = field.substring(pt+3).trim();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }

  }
}
