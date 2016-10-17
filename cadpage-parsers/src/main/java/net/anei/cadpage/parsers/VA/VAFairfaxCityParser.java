package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class VAFairfaxCityParser extends FieldProgramParser {
  
  private static final Pattern SENT_ON_PTN = Pattern.compile("\n\nSent on: (\\d\\d/\\d\\d) (\\d\\d:\\d\\d)$");
  private static final Pattern DATE_MARKER = Pattern.compile("^Date:.*\n+");
  
  public VAFairfaxCityParser() {
    super("FAIRFAX", "VA",
          "Box:BOX! Event:CODE! Adrs:ADDR! Note:INFO+ Ch:CH");
    setBreakChar(' ');
  }
  
  @Override
  public String getFilter() {
    return "msg@rsan.fairfaxva.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Subject goes into call description
    if (subject.length() > 0 && !subject.equals("<no data>")) {
      data.strCall = subject;
    }
    
    int pt = body.indexOf("\n\nSent by");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = SENT_ON_PTN.matcher(body);
    if (!match.find()) return false;
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    body = body.substring(0,match.start()).trim();
    
    match = DATE_MARKER.matcher(body);
    if (match.find()) body = body.substring(match.end()).trim();
    
    return parseFields(body.split("/\n"), 4, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }
  
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      
      // If we didn't get a call description from the subject, make 
      // this the call description, otherwise it is the call code
      if (data.strCall.length() > 0) {
        data.strCode = field;
      } else {
        data.strCall = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("BOX")) return new BoxField("\\d+", true);
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d +\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  
}
