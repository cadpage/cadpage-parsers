package net.anei.cadpage.parsers.MT;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MTLewisAndClarkCountyBParser extends FieldProgramParser {

  public MTLewisAndClarkCountyBParser() {
    super("LEWIS AND CLARK COUNTY", "MT",
           "REQ:INFO! LOC:ADDR! TYPE:INFO!");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    data.strCall = subject;
    body = body.replace('\n', ' ');
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
}
