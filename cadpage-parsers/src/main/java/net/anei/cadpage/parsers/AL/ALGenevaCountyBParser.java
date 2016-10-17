
package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Baldwin County, AL
 */
public class ALGenevaCountyBParser extends FieldProgramParser {

  public ALGenevaCountyBParser() {
    super("GENEVA COUNTY", "AL",
          "CALL! Location:ADDR! City:CITY! Subject:CALL! Place:PLACE! Details:INFO!");
  }
    
  @Override
  public String getFilter() {
    return "34Central@smtp.gmail.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("34 Central:_")) return false;
    body = body.substring(12).trim();
    return super.parseFields(body.split(" //"), data);
  }
}
