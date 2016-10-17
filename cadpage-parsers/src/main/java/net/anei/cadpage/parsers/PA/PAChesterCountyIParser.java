package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;




public class PAChesterCountyIParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyIParser() {
    super("CALL ADDRCITY X CITY BOX NAME PHONE CODE! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "EWFC05@verizon.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(" *")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("BOX")) return new BoxField("\\d+", true);
    if (name.equals("CODE")) return new CodeField("[A-Z]+", true);
    return super.getField(name);
  }
} 
