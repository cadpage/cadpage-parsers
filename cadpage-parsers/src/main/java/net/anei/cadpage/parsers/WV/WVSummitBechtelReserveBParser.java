package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WVSummitBechtelReserveBParser extends FieldProgramParser {

  public WVSummitBechtelReserveBParser() {
    super("SUMMIT BECHTEL RESERVE", "WV",
          "New_Incident_Created%EMPTY! Inc#:ID! Type:CALL! Created:DATETIME! Location:EMPTY! PLACE! ADDR! EMPTY CITY_ST! EMPTY INFO/N+ Comments:INFO/N+ https:SKIP! END");
  }

  @Override
  public String getFilter() {
    return "cad@mail.incidentcad.com,cad@mail.incidentcad.dev";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("New - ")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("CITY_ST")) return new MyCityStateField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strCode = field.substring(0, pt).trim();
        field = field.substring(pt+3).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyCityStateField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        data.strState = field.substring(pt+1).trim();
        field = field.substring(0, pt);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
}
