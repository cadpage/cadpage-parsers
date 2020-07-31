package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLGulfCountyParser extends FieldProgramParser {
  
  public FLGulfCountyParser() {
    super("GULF COUNTY", "FL", 
          "CALL! ADDRESS:ADDR! PLACE! RX:DATETIME! CAD#:ID! UNIT:UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "smartcop@gcso.fl.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CALL FOR SERVICE")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
