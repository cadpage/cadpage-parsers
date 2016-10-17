package net.anei.cadpage.parsers.WI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class WIKenoshaCountyBParser extends FieldProgramParser {
  
  private static final Pattern MASTER = 
    Pattern.compile("\\d+[: ] CAD@plprairiewi.com Subject:Phoenix Notification (\\d+) ä(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)ñ (.*)ä(\\d)ñ (.*)");
  
  public WIKenoshaCountyBParser() {
    super("KENOSHA COUNTY", "WI",
          "ADDR! Units:UNIT Comments:INFO");
  }
  
  @Override
  public String getFilter() {
    return "CAD@plprairiewi.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    data.strCall = match.group(4).trim();
    data.strPriority = match.group(5);
    body = match.group(6).trim();
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "ID DATE TIME CALL PRI " + super.getProgram();
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() +  " PLACE";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
}
