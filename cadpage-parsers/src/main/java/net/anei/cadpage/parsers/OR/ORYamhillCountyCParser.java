package net.anei.cadpage.parsers.OR;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class ORYamhillCountyCParser extends FieldProgramParser {
  
  public ORYamhillCountyCParser() {
    super("YAMHILL COUNTY", "OR", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! MAP:MAP% UNIT:UNIT%");
  }
  
  @Override
  public String getFilter() {
    return "ycom@ycom911.org";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern DELIM = Pattern.compile("\\* ");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Inform CAD Page")) return false;
    return parseFields(DELIM.split(body), data);
  }
}
