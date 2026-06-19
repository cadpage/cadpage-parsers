package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCRichlandCountyBParser extends FieldProgramParser {

  public SCRichlandCountyBParser() {
    super("RICHLAND COUNTY", "SC",
          "CALL ADDR ( UNIT DATETIME SRC ID ID/L GPS1 GPS2! " + 
                    "| PLACE CITY ST GPS UNIT! " + 
                    ") END");
  }

  @Override
  public String getFilter() {
    return "noreply@alastar.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(.*?) +(?:Response Alert|CAD)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    body = stripFieldStart(body, subject);
    return parseFields(body.split("\n+"), data);
  }

  @Override
  public String getProgram() {
    return "SRC? " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d) (\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(4);
      data.strTime = match.group(4);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
