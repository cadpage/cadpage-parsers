package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  private static final Pattern PFX1_PTN = Pattern.compile("(?:\\*+(?:ALL CALL)\\*+:?|ALL )(?=CALL:)");
  private static final Pattern PFX2_PTN = Pattern.compile("\\*+2ND PAGE\\*+ *\n");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher  match = PFX1_PTN.matcher(body);
    if (match.lookingAt()) {
      body = body.substring(match.end());
    } else if ((match = PFX2_PTN.matcher(body)).lookingAt()) {
      body = "CALL:" + body.substring(match.end());
    }
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
