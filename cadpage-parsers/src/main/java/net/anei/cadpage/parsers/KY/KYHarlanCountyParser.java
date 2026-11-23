package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KYHarlanCountyParser extends FieldProgramParser {

  public KYHarlanCountyParser() {
    super("HARLAN COUNTY", "KY",
          "CALL:CALL! CODE:CODE! PRI:PRI! PLACE:PLACE? ADDR:ADDRCITY CITY:CITY! GPS:GPS! MAP:SKIP! INFO:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "dispatch@guardiancad911.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
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
