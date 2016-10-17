package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ARPopeCountyParser extends FieldProgramParser {
  
  public ARPopeCountyParser() {
    super("POPE COUNTY", "AR",
          "PLACE Location:ADDRCITY! Cross_Street:X Type:CALL! Units:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "alerts@popeco911.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = stripFieldEnd(body, "~");
    return parseFields(body.split("\n"), data);
  }
}
