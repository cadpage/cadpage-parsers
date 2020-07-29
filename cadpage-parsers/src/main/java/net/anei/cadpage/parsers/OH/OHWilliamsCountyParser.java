package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHWilliamsCountyParser extends FieldProgramParser {

  public OHWilliamsCountyParser() {
    super("WILLIAMS COUNTY", "OH",
          "FIRE_CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! INFO:INFO/N+");
  }

  @Override
  public String getFilter() {
    return "BryanCAD,info@sundance-sys.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

}
