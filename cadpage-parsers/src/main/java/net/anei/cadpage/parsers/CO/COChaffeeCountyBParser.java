package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COChaffeeCountyBParser extends FieldProgramParser {

  public COChaffeeCountyBParser() {
    super("CHAFFEE COUNTY", "CO", 
          "Call_Number:ID! Call_Type:CALL! GPS:GPS! CALL_NOTES%EMPTY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "@chaffeecountysarnorth.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\nInitiated By:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }
}
