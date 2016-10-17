package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class PADelawareCountyEParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile("\\*\\*");
  
  public PADelawareCountyEParser() {
    super("DELAWARE COUNTY", "PA",
           "UPDT? CALL ADDR/SXP X X TIME DATE! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "bfc53@comcast.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), 6, data);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('~', ' ');
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UPDT")) return new SkipField("|\\[Update\\]");
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
}
