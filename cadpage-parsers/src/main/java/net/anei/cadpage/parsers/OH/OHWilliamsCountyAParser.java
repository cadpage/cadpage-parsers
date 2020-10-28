package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHWilliamsCountyAParser extends FieldProgramParser {

  public OHWilliamsCountyAParser() {
    super("WILLIAMS COUNTY", "OH",
          "FIRE_CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! INFO:INFO/N+");
  }

  @Override
  public String getFilter() {
    return "BryanCAD,info@sundance-sys.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("Active 911")) return false;
    return parseFields(body.split("\n"), data);
  }

}
