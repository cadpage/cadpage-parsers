package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class NYNiagaraCountyCParser extends DispatchProQAParser {
  
  public NYNiagaraCountyCParser() {
    super("NIAGARA COUNTY", "NY", 
          "ID! CALL CALL/L+? TIME ADDR APT CITY ZIP! INFO/N+", true);
  }
  
  @Override
  public String getFilter() {
    return "777";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("ZIP")) return new MyZipField();
    return super.getField(name);
  }
  
  private class MyZipField extends Field {
    
    public MyZipField() {
      super("\\d{5}|", true);
    }

    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() == 0) data.strCity = field;
    }

    @Override
    public String getFieldNames() {
      return "CITY";
    }
  }

}
