package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXCookeCountyParser extends FieldProgramParser {
  
  public TXCookeCountyParser() {
    super("COOKE COUNTY", "TX", 
        "CALL ADDR CITY! CrossStreets:X! PLACE! Description:INFO! INFO/N+ Dispatch:DATETIME!");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@co.cooke.tx.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d");
    return super.getField(name);
  }

}
