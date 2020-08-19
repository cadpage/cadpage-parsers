package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHLoganCountyBParser extends FieldProgramParser {

  public OHLoganCountyBParser() {
    super("LOGAN COUNTY", "OH",
           "CFS:ID! RUN#:SKIP! CODE:CALL! PLACE:PLACE! LAT:GPS1! LONG:GPS2! ADDR:ADDR! CITY:CITY! CROSS:X! PRI:PRI! DATE:DATE! TIME:TIME! SECT:MAP! DIST:SRC! UNIT:UNIT! DESC:INFO/N+");
  }

  @Override
  public String getFilter() {
    return "lcso911@co.logan.oh.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CFS:")) return false;
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0, pt).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "&");
      super.parse(field, data);
    }
  }

}
