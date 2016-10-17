package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYNassauCountyBethpageParser extends FieldProgramParser {
  
  private static final Pattern MARKER = Pattern.compile("^(?:10 - WORKING FIRE)?(\\d{4}-\\d{6}) +(\\d+)\\) +");

  public NYNassauCountyBethpageParser() {
    super("NASSAU COUNTY", "NY",
          "CALL! ADDR/SXa! **_CS:X? EMPTY? TOA:TIMEDATE! SRC!");
  }
  
  @Override
  public String getFilter() {
    return "900bfd@gmail.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    data.strCallId = match.group(1);
    data.strCode = match.group(2);
    body = body.substring(match.end());
    return parseFields(body.split("\n"), data);
  };
  
  @Override
  public String getProgram() {
    return "ID CODE " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIMEDATE")) return new MyTimeDateField();
    return super.getField(name);
  }
  
  private class MyTimeDateField extends TimeDateField {
    public MyTimeDateField() {
      super("\\d\\d:\\d\\d \\d\\d-\\d\\d-\\d\\d", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      super.parse(field.replace('-', ':'), data);
    }
  }
}


