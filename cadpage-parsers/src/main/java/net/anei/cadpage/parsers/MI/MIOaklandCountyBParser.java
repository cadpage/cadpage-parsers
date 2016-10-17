package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIOaklandCountyBParser extends FieldProgramParser {
  
  public MIOaklandCountyBParser() {
    super("OAKLAND COUNTY", "MI", 
          "Run:ID! Calltype:CALL! Time:TIMEDATE! Address:ADDRCITY! Comments:INFO+");
  }
  
  @Override
  public String getFilter() {
    return "otfdcad@firemodules.net";
  }
  
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CAD Alert : Run ")) return false;
    if (body.endsWith(" -")) body += ' ';
    return parseFields(body.split(" - "), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{6}", true);
    if (name.equals("TIMEDATE")) return new MyTimeDateField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern TIME_DATE_PTN = Pattern.compile("(\\d{2})(\\d{2}) +\\((\\d\\d/\\d\\d)\\)");
  private class MyTimeDateField extends TimeDateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1)+':'+match.group(2);
      data.strDate = match.group(3);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Original Location :");
      data.strSupp = append(data.strSupp, " - ", field);
    }
  }
}
