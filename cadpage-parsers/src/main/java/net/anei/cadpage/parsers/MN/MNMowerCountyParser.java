package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNMowerCountyParser extends FieldProgramParser {

  public MNMowerCountyParser() {
    super("MOWER COUNTY", "MN", 
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR! CITY:CITY? ID:ID! PRI:PRI? INFO:INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split(";"), data)) return false;
    
    // One agency likes to duplicate the city name in the address field :(
    if (data.strCity.length() > 0) {
      data.strAddress = stripFieldEnd(data.strAddress, " " + data.strCity);
    }
    return true;
  }
}
