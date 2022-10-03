package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXWichitaCountyBParser extends FieldProgramParser {

  public TXWichitaCountyBParser() {
    super("WICHITA COUNTY", "TX",
          "CALL PLACE ADDR CH! END");
  }

  @Override
  public String getFilter() {
    return "PublicSafety@WichitaFallsTX.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Enterprise CAD System Page")) return false;
    return parseFields(body.split(":"), data);
  }

}
