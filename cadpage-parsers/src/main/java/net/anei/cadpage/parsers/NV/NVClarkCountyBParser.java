package net.anei.cadpage.parsers.NV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NVClarkCountyBParser extends FieldProgramParser {
  
  private static final Pattern UNIT_ID_PTN = Pattern.compile("^(\\d{2,4}) (\\d{8}) +");
  
  public NVClarkCountyBParser() {
    super("CLARK COUNTY", "NV",
           "Pri:PRI! Prob:CODE! Grid:ADDR! Bld:APT! Apt:APT! Zip:CITY!");
  }
  
  @Override
  public String getFilter() {
    return "sms@pageway.net";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    do {
      
      if (subject.equals("SMS")) break;
      
      if (body.startsWith("SMS / ")) {
        body = body.substring(6).trim();
        break;
      }
      
      return false;
      
    } while (false);
    
    // Parse unit and call ID
    Matcher match = UNIT_ID_PTN.matcher(body);
    if (!match.find()) return false;
    data.strUnit = match.group(1);
    data.strCallId = match.group(2);
    body = body.substring(match.end()).trim();
    
    // See if this is a run report
    if (body.contains("Disp:")) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      return true;
    }
    
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "CALL PLACE UNIT ID " + super.getProgram();
  }
  
  private static final Pattern MAP_PTN = Pattern.compile("^(\\d{4,5}-(?:\\d{1,2}/)?\\d\\d) +");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_PTN.matcher(field);
      if (!match.find()) abort();
      data.strMap = match.group(1);
      field = field.substring(match.end());
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "MAP " + super.getFieldNames();
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, "-", field);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
}
