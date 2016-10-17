package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAEffinghamCountyBParser extends FieldProgramParser {
  
  public GAEffinghamCountyBParser() {
    super("EFFINGHAM COUNTY", "GA", 
          "CALL:CALL! ( ADDR:ADDR! | LOCATION:ADDR! ) CITY:CITY! INFO:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@EffinghamCounty.org>";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\nThis e-mail");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }

}
