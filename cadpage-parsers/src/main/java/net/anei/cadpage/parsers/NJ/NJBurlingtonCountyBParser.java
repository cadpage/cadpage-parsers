package net.anei.cadpage.parsers.NJ;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NJBurlingtonCountyBParser extends FieldProgramParser {
  
  public NJBurlingtonCountyBParser() {
    super("BURLINGTON COUNTY", "NJ",
          "UPDT? TIME CALL UNIT ADDR! X X INFO+");
  }
  
  @Override
  public String getFilter() {
    return "chief1300@comcast.net";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    do {
      if (subject.startsWith("Messenger 911")) break;
      if (body.startsWith("Messenger 911 / ")) {
        body = body.substring(16).trim();
        break;
      }
      return false;
    } while (false);
    String[] flds = body.split("\n");
    if (flds.length < 4) return false;
    return parseFields(flds, data);
  }
  
  private static final Pattern PHONE_PTN = Pattern.compile("\\d{3}[-\\.]?\\d{3}[-\\.]?\\d{4}");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (PHONE_PTN.matcher(field).matches()) {
        data.strPhone = field;
      } else {
        data.strSupp = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PHONE INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UPDT")) return new SkipField("\\[Update\\]", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
}
