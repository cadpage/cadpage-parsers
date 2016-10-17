package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA8Parser;

public class NYLivingstonCountyBParser extends DispatchA8Parser {
  
  public NYLivingstonCountyBParser() {
    super("LIVINGSTON COUNTY", "NY");
  }
  
  @Override
  public String getFilter() {
    return "hemlockfd@rochester.rr.com,hemlockfire@rochester.twcbc.com,@CO.LIVINGSTON.NY.US";
  }

  private class MyCrossField extends CrossField  {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('-');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
  
  @Override
  protected Field getField(String name) {
    // They put cross street info in what is usually the place name
    if (name.equals("PLACE")) return new MyCrossField();
    return super.getField(name);
  }
}
	