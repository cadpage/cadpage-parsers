package net.anei.cadpage.parsers.general;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Parser class that handles standard text format A
 */
public class StandardAParser  extends FieldProgramParser{
  
  public StandardAParser() {
    super("", "", 
           "CALL ADDR CITY INFO");
  }
  
  @Override
  public String getLocName() {
    return "Standard Format A";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!isPositiveId()) return false;
    return parseFields(body.split(";"), data);
  }
}
