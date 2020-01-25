package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class NYErieCountyHParser extends DispatchProQAParser {
  
  public NYErieCountyHParser() {
    super("ERIE COUNTY", "NY", 
          "ID! CALL CALL/L+? TIME ADDR APT CITY ZIP CALL/L INFO/N+", true);
  }
  
  @Override
  public String getFilter() {
    return "2082524750";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("TWIN CITY CAD: ")) return false;
    body = body.substring(15).trim();
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("ZIP")) return new MyZipField();
    return super.getField(name);
  }
  
  private class MyZipField extends CityField {
    
    public MyZipField() {
      super("\\d{5}|", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      super.parse(field, data);
    }
  }

}
