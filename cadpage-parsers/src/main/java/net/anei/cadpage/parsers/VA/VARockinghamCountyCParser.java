package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VARockinghamCountyCParser extends FieldProgramParser {

  public VARockinghamCountyCParser() {
    super("ROCKINGHAM COUNTY", "VA",
          "Call:CALL! Address:ADDRCITY! City/Town:CITY? Business:PLACE? X_Street:X! Incident_#:ID! Tac:CH! Priority:PRI! Units:UNIT! Fire_Box:BOX? Lat:GPS1! Lon:GPS2! CAD_#:ID/L Move_Up:INFO Standby:INFO INFO/N+");
  }

  @Override
  public String getFilter() {
    return "caddmsmailbox@hrecc.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("PRI")) return new PriorityField("(\\d) - .*", false);
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      super.parse(field, data);
    }
  }
}
