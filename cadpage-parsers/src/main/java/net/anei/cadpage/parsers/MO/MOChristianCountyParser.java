package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Christian County, MO
 */
public class MOChristianCountyParser extends FieldProgramParser {

  public MOChristianCountyParser() {
    super("CHRISTIAN COUNTY", "MO", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! ID:ID! XST:X! DATE:DATETIME! UNIT:UNIT! INFO:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "ccespage@cces911.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
