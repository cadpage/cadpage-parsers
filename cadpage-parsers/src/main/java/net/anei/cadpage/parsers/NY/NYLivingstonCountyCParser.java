package net.anei.cadpage.parsers.NY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYLivingstonCountyCParser extends FieldProgramParser {
  
  public NYLivingstonCountyCParser() {
    super(NYLivingstonCountyAParser.CITY_CODES, "LIVINGSTON COUNTY", "NY",
          "Call_Type:CODE! Address:ADDRCITY! Common:PLACE! X-street:X! Name:NAME! Nature:CALL! UNIT! Narrative:INFO! INFO/N+");
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
    if (name.equals("UNIT")) return new UnitField("Asg Units +(.*)", true);
    return super.getField(name);
  }
}
