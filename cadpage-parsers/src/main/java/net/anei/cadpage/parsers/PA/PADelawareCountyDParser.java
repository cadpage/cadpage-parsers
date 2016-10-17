package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class PADelawareCountyDParser extends FieldProgramParser {
  
  public PADelawareCountyDParser() {
    super("DELAWARE COUNTY", "PA",
           "TIME! ADDR CALL X/Z+? DATE CALL2 UNIT ID INFO");
  }
  
  @Override
  public String getFilter() {
    return "oakmont1@comcast.net";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) {
        super.parse(field, data);
      }
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('~', ' ').trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d\\d\\d", true);
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("ID")) return new IdField("F\\d{8}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
}
