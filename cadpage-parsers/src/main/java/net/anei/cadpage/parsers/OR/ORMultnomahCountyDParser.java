package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORMultnomahCountyDParser extends FieldProgramParser {

  public ORMultnomahCountyDParser() {
    super("MULTNOMAH COUNTY", "OR",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! UNIT:UNIT! INFO:INFO INFO/N+");
  }

  @Override
  public String getFilter() {
    return "@tosohquartz.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("CALL:");
    if (pt < 0) return false;
    body = body.substring(pt);
    if (body.contains("\n")) {
      return parseFields(body.split("\n"), data);
    } else {
      return parseFields(body.split(", "), data);
    }
  }
}
