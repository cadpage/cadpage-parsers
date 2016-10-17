package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class VAHalifaxCountyParser extends DispatchA47Parser {
  
  public VAHalifaxCountyParser() {
    super("from Central", null, "HALIFAX COUNTY", "VA", null);
  }
  
  @Override
  public String getFilter() {
    return "halifaxeoc@co.halifax.va.us";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("\n\n", "\n");
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.toUpperCase().startsWith("TURBEVILLE")) data.strCity = data.strCity.substring(0,10);
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }
  
  private class MyPlaceField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.getLast("  ");
      data.strPlace = p.get();
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE CITY";
    }
  }

}