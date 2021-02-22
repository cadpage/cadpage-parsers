package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHSummitCountyHParser extends FieldProgramParser {

  public OHSummitCountyHParser() {
    super("SUMMIT COUNTY", "OH",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR/S6! CITY:CITY! ID:ID! UNIT:UNIT! ( PRI:MAP! INFO:INFO! INFO/N+ MAP:X! | INFO:INFO! INFO/N+ XSTREET:X WS:MAP )");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "Dispatch1,info@sundance-sys.com,BHDispMap";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("**ALL CALL**:")) body = "CALL:" + body.substring(13);
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" ", "");
      super.parse(field, data);
    }
  }

  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "WS -");
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "7854 MAIN STREET",                     "+40.926976,-81.629593"
  });
}
