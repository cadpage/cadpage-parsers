package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class MOBuchananCountyDParser extends DispatchH05Parser {

  public MOBuchananCountyDParser() {
    super(MOBuchananCountyAParser.CITY_CODES, "BUCHANAN COUNTY", "MO",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! CROSS_ST:X! ID:ID! DATE:DATETIME! UNIT:UNIT! INFO:EMPTY! INFO_BLK+? EXTERNAL_EMAIL:SKIP");
  }

  @Override
  public String getFilter() {
    return "@stjoemo.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\nEXTERNAL EMAIL:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("[", "").replace("]", "");
      if (field.startsWith("Incident not yet created")) return;
      super.parse(field, data);
    }
  }

}
