package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIMacombCountyParser extends FieldProgramParser {
  
  public MIMacombCountyParser() {
    super("MACOMB COUNTY", "MI", 
          "ADDR:ADDR! CITY:CITY! TYPE:CALL! INFO:INFO!");
  }
  
  @Override
  public String getFilter() {
    return "RichmondPaging@comcast.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(" ; "), data);
  }

}
