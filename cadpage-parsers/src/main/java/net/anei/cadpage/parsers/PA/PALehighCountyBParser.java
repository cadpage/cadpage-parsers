package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Lehigh County PA
 */
public class PALehighCountyBParser extends FieldProgramParser {
  
  public PALehighCountyBParser() {
    super("LEHIGH COUNTY", "PA",
          "CALL ADDR CITY APT! INFO+? ID");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@lvh.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!body.startsWith("RC:")) return false;
    body = body.substring(3).trim();
    return parseFields(body.split("/", -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("Run# *(\\d*)", true);
    return super.getField(name);
  }
}
