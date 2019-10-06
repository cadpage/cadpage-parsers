package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MOMoniteauCountyParser extends FieldProgramParser {
  
  public MOMoniteauCountyParser() {
    super("MONITEAU COUNTY", "MO",
          "Call:CALL! Sub:CALL/S! Place:PLACE! Address:ADDR! City:CITY! Apt:APT! Cross_Streets:X! Event#:ID! Reporting_Person:NAME! Phone#:PHONE! Unit:UNIT! Initiated:SKIP! Notes:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@MONITEAU911.COM";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
}
