package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class VADanvilleBParser extends FieldProgramParser {
  
  public VADanvilleBParser() {
    super("DANVILLE", "VA", 
          "Unit:UNIT! Run:ID! Patient:NAME! INFO/N+? Pickup:EMPTY! PLACE? ADDR/Z! APT! CITY_ST_ZIP! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "ServerAlerts@dlsc.org";
  }
  
  private static final Pattern PREFIX = Pattern.compile("Dispatch Message - (Alert|At Destination) - ");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Trip")) return false;
    
    Matcher match = PREFIX.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCall = match.group(1);
    body = body.substring(match.end()).trim();
    
    if (data.strCall.equals("At Destination")) data.msgType = MsgType.RUN_REPORT;
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("CITY_ST_ZIP")) return new BaseCityStateZipField();
    return super.getField(name);
  }
  
  private class BaseAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "RM ");
      super.parse(field, data);
    }
  }
  
  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("([^,]+?), *([A-Z]{2})(?: +\\d{5})?");
  private class BaseCityStateZipField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CITY_ST_ZIP_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCity = match.group(1).trim();
      data.strState = getOptGroup(match.group(2));
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
}
