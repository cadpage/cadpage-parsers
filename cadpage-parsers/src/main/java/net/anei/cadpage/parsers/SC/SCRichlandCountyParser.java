package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class SCRichlandCountyParser extends FieldProgramParser {
 
  public SCRichlandCountyParser() {
    super("RICHLAND COUNTY", "SC",
          "Address:ADDR! Problem:CALL! Facility:PLACE MapGrid:MAP");
  }
  
  @Override
  public String getFilter() {
    return "2002000004,@alert.active911.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.equals("911 Page") && body.startsWith("Comment:")) {
      data.strCall = "GENERAL ALERT";
      data.strPlace = body.substring(9).trim();
      return true;
    }
    
    // If the regular message parser handles this all is well
    body = body.replace(" MapGrid ", " MapGrid:");
    if (!super.parseMsg(body, data)) {
      
      // If not, see if we can get this through a general type parser
      // Which will only accept it caller has identified this as a dispatch page
      if (!isPositiveId()) return false;
      
      parseAddress(StartType.START_CALL, FLAG_IGNORE_AT | FLAG_NO_IMPLIED_APT, body, data);
      if (data.strAddress.length() == 0) return false;
      data.strSupp = getLeft();
    }
    
    // Fix mistyped address
    data.strAddress = data.strAddress.replace("Hardscrabble", "Hard Scrabble");
    data.strAddress = data.strAddress.replace("HARDSCRABBLE", "HARD SCRABBLE");
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final Pattern CALL_UNIT_PTN = Pattern.compile("(.*) ([A-Z]+\\d+(?:,[A-Z0-9,]+)?)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_UNIT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strUnit = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL UNIT";
    }
  }
}
