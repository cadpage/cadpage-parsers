package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALColbertCountyBParser extends FieldProgramParser {
  
  public ALColbertCountyBParser() {
    super("COLBERT COUNTY", "AL", 
          "ID! TYPE:CALL! LOC:ADDR! CITY:CITY! GPS UNIT:UNIT! Comments:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "colbertcoal@911email.net,e-alerts@colbert911.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("DISPATCH")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("INCIDENT # *(.*)", true);
    if (name.equals("GPS")) return new GPSField("[-+]?\\d{1,3}\\.\\d{6,} *, *[-+]?\\d{1,3}\\.\\d{6,}", true);
    return super.getField(name);
  }
}
