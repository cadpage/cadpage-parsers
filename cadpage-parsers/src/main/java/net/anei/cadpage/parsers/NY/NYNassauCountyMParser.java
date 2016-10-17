package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYNassauCountyMParser extends FieldProgramParser {
  
  private static final Pattern ID_PTN = Pattern.compile("\\b\\d{4}-\\d{6}$");
  
  public NYNassauCountyMParser() {
    super("NASSAU COUNTY", "NY",
          "TOA:TIMEDATE! CALL CALL+? ADDRCITY! NAME CS:X");
  }
  
  @Override
  public String getFilter() {
    return "alarms@oceansidefd.net,paging@oceansidefd.info";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = ID_PTN.matcher(body);
    if (!match.find()) return false;;
    data.strCallId = match.group();
    body = body.substring(0,match.start()).trim();
    return parseFields(body.split("\n"), 4, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " ID";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIMEDATE")) return new TimeDateField("\\d\\d:\\d\\d \\d\\d-\\d\\d-\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }
}
