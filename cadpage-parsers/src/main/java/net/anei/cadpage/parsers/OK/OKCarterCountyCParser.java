package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OKCarterCountyCParser extends FieldProgramParser {

  public OKCarterCountyCParser() {
    super("CARTER COUNTY", "OK",
          "CALL ADDR CITY! INFO");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n-- ");
    if (pt < 0) return false;
    String left = body.substring(pt+4).trim();
    body = body.substring(0,pt).trim();
    if (!left.equals("Lone Grove Communications")) return false;

    return parseFields(body.split(";", 4), data);
  }

}
