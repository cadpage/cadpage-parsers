package net.anei.cadpage.parsers.MT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MTCascadeCountyAParser extends FieldProgramParser {
  
  public MTCascadeCountyAParser() {
    super("CASCADE COUNTY", "MT",
          "DATETIME UNIT ADDR EMPTY CALL! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "911text@911intn.org,@greatfallsmt.net";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Emergency Message")) return false;
    return parseFields(body.split("\n"), 5, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]+");
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d) (\\d\\d)(\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2)+':'+match.group(3);
    }
  }
}
