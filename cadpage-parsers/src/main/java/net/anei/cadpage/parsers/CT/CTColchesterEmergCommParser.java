package net.anei.cadpage.parsers.CT;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class CTColchesterEmergCommParser extends FieldProgramParser {
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("Unit: ([A-Z0-9]+) (Times: )# (\\d\\d-\\d+) (.*)");
  
  public CTColchesterEmergCommParser() {
    super("", "CT",
          "CITY ADDR APT X CALL PLACENAME ID! EXTRA");
  }
  
  @Override
  public String getLocName() {
    return "Colchester Emergency Communications, CT";
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,911@kx911.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    data.strSource = subject;
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      data.strCall = "RUN REPORT";
      data.strUnit = match.group(1);
      data.strCallId = match.group(3);
      data.strPlace = match.group(2) + match.group(4).trim();
      return true;
    }
    
    return parseFields(body.split("\\\\"), 7, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("@")) abort();
      super.parse(field.substring(1).trim(), data);
    }
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("** ")) abort();
      super.parse(field.substring(3).trim(), data);
    }
  }
  
  private static final Pattern EXTRA_PTN = Pattern.compile("(\\d\\d:\\d\\d)\\b.*?(?:\\((.*)\\))?$");
  private class ExtraField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = EXTRA_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1);
      data.strSupp = getOptGroup(match.group(2));
    }
    
    @Override
    public String getFieldNames() {
      return "TIME INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("EXTRA")) return new ExtraField();
    return super.getField(name);
  }
}
