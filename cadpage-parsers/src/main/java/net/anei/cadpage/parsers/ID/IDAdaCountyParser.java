package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IDAdaCountyParser extends FieldProgramParser {
  
  public IDAdaCountyParser() {
    super("ADA COUNTY", "ID", 
          "ID:ID! CALL:CALL! ADDR:ADDR! CITY:CITY! LAT:GPS1? LONG:GPS2? PRI:PRI! DATE:DATE! TIME:TIME! INFO:INFO/N+ UNIT:UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "cad@gocai.com,active911@co.gem.id.us,dispatch@co.fremont.id.us";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }

}
