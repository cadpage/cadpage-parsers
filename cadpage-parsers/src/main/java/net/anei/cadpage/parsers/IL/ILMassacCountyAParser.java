
package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Massac County, IL
 */
public class ILMassacCountyAParser extends FieldProgramParser {

  public ILMassacCountyAParser() {
    super("MASSAC COUNTY", "IL",
          "CALL ADDR CITY INFO! INFO/N+");
  }
    
  @Override
  public String getFilter() {
    return "page@joppafd.com";
  }
  
  private static Pattern SRC_DATE_TIME_PTN = Pattern.compile("(.*?) Page Received at (\\d\\d:\\d\\d:\\d\\d) on (\\d\\d/\\d\\d/\\d\\d)\n?");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SRC_DATE_TIME_PTN.matcher(body);
    if (match.lookingAt()) {
      body = body.substring(match.end()).trim();
    } else {
      match = SRC_DATE_TIME_PTN.matcher(subject);
      if (!match.matches()) return false;
    }
    data.strSource = match.group(1).trim();
    data.strTime = match.group(2);
    data.strDate = match.group(3);
    
    return parseFields(body.split(";"), 4, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC TIME DATE " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("https://");
      if (pt >= 0) {
        data.strInfoURL = field.substring(pt);
        field = field.substring(0,pt).trim();
      }
      if (field.endsWith("PAGE RECEIVED")) return;
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " URL";
    }
  }
}
