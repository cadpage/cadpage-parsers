package net.anei.cadpage.parsers.NY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYLivingstonCountyCParser extends FieldProgramParser {
  
  public NYLivingstonCountyCParser() {
    super(NYLivingstonCountyAParser.CITY_CODES, "LIVINGSTON COUNTY", "NY",
          "Call_Type:CODE! Address:ADDRCITY! Common:PLACE! X-street:X! Name:NAME! Nature:CALL! CR#:ID! Asg_Units:UNIT! Narrative:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "donotrespond@co.livingston.ny.us";
  }
  
  private static final Pattern DELIM = Pattern.compile("::|\n");
  
  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("   ");
      if (pt >= 0) field = field.substring(0, pt);
      super.parse(field, data);
    }
  }
}
