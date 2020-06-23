package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAMaderaCountyParser extends FieldProgramParser {
  
  public CAMaderaCountyParser() {
    super("MADERA COUNTY", "CA", 
          "CALL ADDR CITY! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@CityOfChowchilla.org";
  }
  
  private static final Pattern SUBJECT_ID_PTN = Pattern.compile("(?:CAD EVENT NUMBER|EVENT) +(\\d+)");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() > 0) {
      Matcher match = SUBJECT_ID_PTN.matcher(subject);
      if (match.matches()) data.strCallId = match.group(1);
    }
    
    int pt = body.indexOf("\n");
    if(pt < 0) return false;
    body = body.substring(0,pt).trim();
    
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new MyTimeField();
    return super.getField(name);
  }
  
  private static final Pattern TIME_PTN = Pattern.compile("(\\d{2})(\\d{2})");
  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1) +':' + match.group(2);
    }
  }
}
