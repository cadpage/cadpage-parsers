package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class VAPetersburgParser extends FieldProgramParser {

  public VAPetersburgParser() {
    super("PETERSBURG", "VA",
          "ID UNIT CALL ADDRCITYST! XST1:X! XST2:X! NOTES:INFO! END");
  }

  @Override
  public String getFilter() {
    return "CAD@petersburg-police.com,cad@petersburg-va.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(" \\| "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\$([A-Z]{1,5}\\d{2}-\\d{6})", true);
    return super.getField(name);
  }
}
