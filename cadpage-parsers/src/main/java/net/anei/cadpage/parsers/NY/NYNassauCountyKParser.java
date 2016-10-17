package net.anei.cadpage.parsers.NY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYNassauCountyKParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile("\n(?:\\*\\* )?");
  
  public NYNassauCountyKParser() {
    super("NASSAU COUNTY", "NY",
          "CALL ADDR! CS:X EMPTY TOA:TIMEDATE! ID! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "@firerescuesystems.xohost.com,wantaghpaging@gmail.com,paging@wantaghfireinfo.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Reject NYNassauCountyH messages
    if (body.startsWith("**")) return false;
    
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIMEDATE")) return new TimeDateField("\\d\\d:\\d\\d \\d\\d-\\d\\d-\\d\\d", true);
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{6}", true);
    return super.getField(name);
  }
}
