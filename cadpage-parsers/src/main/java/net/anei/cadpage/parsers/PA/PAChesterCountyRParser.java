package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyRParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyRParser() {
    super("( DATE TIME DISPATCH CALL ADDRCITY APT CITY! X NAME PHONE UNIT INFO/N+? ID END" +
          "| CALL ADDRCITY! END " + 
          ")");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch02008@prs.FDCMS.info";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    if (body.startsWith("Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{2,4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("DISPATCH")) return new SkipField("DISPATCH", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new IdField("F\\d+", true);
    return super.getField(name);
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_HEADER_PTN = Pattern.compile("\\d\\d?:\\d\\d:\\d\\d \\S+ +");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("[0-9:]+(?: \\S+)?");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      Matcher match = INFO_HEADER_PTN.matcher(field);
      if (match.lookingAt()) {
        field = field.substring(match.end());
      } else if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
    
  }
}
