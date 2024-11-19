package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHUnionCountyBParser extends FieldProgramParser {

  public OHUnionCountyBParser() {
    super("UNION COUNTY", "OH",
          "Call_Type:CODE_CALL! Status:SKIP? Address:ADDRCITY! Cross_Streets:X Lat/Lon:GPS Fire_Box:BOX Radio_Channel:CH EMPTY+? ( Dispatched_Units:UNIT! | Units:UNIT! ) Primary_Incident_Number:ID! Narrative:INFO! INFO/N+");
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
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strCode = field.substring(0, pt).trim();
        field = field.substring(pt+3).trim();
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+3).trim();
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
