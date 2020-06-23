package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PANorthamptonCountyCParser extends FieldProgramParser {

  public PANorthamptonCountyCParser() {
    super("NORTHAMPTON COUNTY", "PA", 
          "ID CALL CITY ADDR EMPTY! END");
  }
  
  @Override
  public String getFilter() {
    return "noreply@norrycopa.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("~", -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.contentEquals("ID")) return new IdField("\\d{10}", true);
    return super.getField(name);
  }
}
