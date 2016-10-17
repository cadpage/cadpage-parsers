package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHLimaParser extends FieldProgramParser {
  
  public OHLimaParser() {
    super("LIMA", "OH", 
          "ID! Time_Out:DATETIME! CALL! ADDR! COMMON_NAME:NAME! CROSS_1:X! CROSS_2:X! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@cityhall.lima.oh.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CAD Page ")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d\\d-\\d{6}", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4})@(\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    }
  }

}
