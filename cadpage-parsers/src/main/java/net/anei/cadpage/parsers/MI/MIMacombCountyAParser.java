package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIMacombCountyAParser extends FieldProgramParser {
  
  public MIMacombCountyAParser() {
    super("MACOMB COUNTY", "MI", 
          "ADDR:ADDR! CITY:CITY! TYPE:CALL! INFO:INFO!");
  }
  
  @Override
  public String getFilter() {
    return "RichmondPaging@comcast.net,richmondpaging@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(" ; "), data);
  }

}
