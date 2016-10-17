package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJAtlanticCountyAParser extends FieldProgramParser {

  public NJAtlanticCountyAParser() {
    super("ATLANTIC COUNTY", "NJ", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! CROSS:X! MAP:MAP DATE:DATETIME INFO:INFO! ID:ID! GPS:GPS!");
  }
  
  @Override
  public String getFilter() {
    return "CAD@ehtpd.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("DISPATCH") && !subject.equals("Dispatch")) return false;

    return super.parseFields(body.split("\n"), data);
  }

  
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}");
    return super.getField(name);
  }
}
