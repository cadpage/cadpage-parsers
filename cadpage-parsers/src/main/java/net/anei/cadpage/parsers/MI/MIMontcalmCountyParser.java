package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class MIMontcalmCountyParser extends DispatchOSSIParser {
  
  public MIMontcalmCountyParser() {
    super("MONTCALM COUNTY", "MI",
           "( FYI DATETIME ADDR CALL! | ADDR ID ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "cad@mydomain.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (! super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) data.strCall = "EMS ALERT";
    return true;
  }
  
  private class MyCallField extends CallField {
    
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Event spawned from ")) field = field.substring(19).trim(); 
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("-Comments:")) field = field.substring(10).trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new IdField("\\d{7}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
}
