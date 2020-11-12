package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHLoganCountyDParser extends FieldProgramParser {

  public OHLoganCountyDParser() {
    super("LOGAN COUNTY", "OH",
          "PLACE:PLACE! ADDR:ADDR! MAP:MAP! CITY:CITY! ID:ID! UNIT:UNIT! INFO:INFO/N+");
  }

  @Override
  public String getFilter() {
    return "info@sundance-sys.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
}
