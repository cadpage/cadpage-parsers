package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INJohnsonCountyParser extends FieldProgramParser {
  
  public INJohnsonCountyParser() {
    super("JOHNSON COUNTY", "IN", 
          "Locution_Dispatch%EMPTY! Units:UNIT! Incident_#:ID! Incident:CALL! Address:ADDR! Common_Place:PLACE! Apartment:APT! Cross:X! City:CITY! Map:MAP! Radio:CH! Comments:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "CADVoice";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Locution Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }

}
