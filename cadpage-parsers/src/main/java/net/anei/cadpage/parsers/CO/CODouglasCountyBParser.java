package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class CODouglasCountyBParser extends FieldProgramParser {
  
  private static final Pattern MASTER1 = Pattern.compile("([^:]+): *(\\d\\d/\\d\\d) +(\\d\\d:\\d\\d)\\b *(.*)");
  private static final Pattern MASTER2 = Pattern.compile("([A-Z]{2}-\\d\\d-[A-Z]) +(.*?) +([A-Z0-9,]+)");
  private static final Pattern FALLBACK_PTN = Pattern.compile("(.*?) +([A-Z]{2}[-/ A-Z0-9]+)");

  public CODouglasCountyBParser() {
    super("DOUGLAS COUNTY", "CO",
          "CALL+? MAP ADDR APT PLACE UNIT/C+");
  }
  
  @Override
  public String getFilter() {
    return "Group_Page_Notification@usamobility.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    int pt = body.indexOf(" Received");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = MASTER1.matcher(body);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    body = match.group(4);
    
    if (body.startsWith("/")) {
      return parseFields(body.substring(1).split("/"), data);
    }
    
    match = MASTER2.matcher(body);
    if (!match.matches()) return false;
    
    setFieldList("MAP ADDR APT CALL UNIT");

    data.strMap = match.group(1);
    String addr = match.group(2).trim();;
    data.strUnit = match.group(3);
    
    // Try using smart address parser to break out address and call description
    parseAddress(StartType.START_ADDR, addr, data);
    data.strCall = getLeft();
    if (data.strCall.length() > 0) return true;
    
    // No go, try a fall back pattern that counts on the call description being upper case
    // and the address being camel case
    data.strAddress = "";
    match = FALLBACK_PTN.matcher(addr);
    if (!match.matches()) return false;
    parseAddress(match.group(1), data);
    data.strCall = match.group(2);
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("MAP")) return new MapField("[A-Z]{2}-\\d{2}-[A-Z]|NOT FOUN", true);
    if (name.equals("UNIT")) return new UnitField("[,A-z0-9]+", true);
    return super.getField(name);
  }
  
  @Override
  public String getProgram() {
    return "SRC DATE TIME " + super.getProgram();
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, "/", field);
    }
  }
  
}
