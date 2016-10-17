package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYSomersParser extends FieldProgramParser {
  
  private static final String[] CITY_LIST = new String[]{
    "LINCOLNDALE", "KATONAH", "SOMERS"
  };
  
  private static final Pattern MARKER = Pattern.compile("^(\\d{4}-\\d{6}) \\*\\*\\* (.+) \\*\\*\\* ");
  private static final Pattern TIME_DATE = Pattern.compile("^\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d\\d ");
  
  public NYSomersParser() {
    super(CITY_LIST, "SOMERS", "NY",
        "ADDR CS:X? TOA:INFO");
  }
  
  @Override
  public String getFilter() {
    return "somersfire@somersfd.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    data.strCallId = match.group(1);
    data.strCall = match.group(2);
    body = body.substring(match.end()).trim();
    return super.parseMsg(body, data);
  }
  
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strName = p.getOptional("***MOS***");
      if (data.strName.length() > 0) {
        parseAddress(StartType.START_ADDR, p.get(), data);
        return;
      }
      
      parseAddress(StartType.START_PLACE, field, data);
      if (data.strAddress.length() == 0) {
        data.strAddress = data.strPlace;
        data.strPlace = "";
      } else if (data.strPlace.contains(",")) {
        data.strName = data.strPlace;
        data.strPlace = "";
      }
    }
    
    @Override
    public String getFieldNames() {
      return "NAME PLACE ADDR CITY";
    }
  }
  
  private class MyInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_DATE.matcher(field);
      if (match.find()) field = field.substring(match.end()).trim();
      field = field.replace("Somers FD", "").replaceAll("  +", " ").trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
    
  }
  
  @Override
  public String getProgram() {
    return "ID CALL " + super.getProgram();
  }
}
