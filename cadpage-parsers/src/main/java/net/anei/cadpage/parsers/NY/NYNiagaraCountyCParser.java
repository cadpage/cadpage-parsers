package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class NYNiagaraCountyCParser extends DispatchProQAParser {
  
  public NYNiagaraCountyCParser() {
    super("NIAGARA COUNTY", "NY", 
          "ID! CALL CALL/L+? TIME ADDR APT CITY MAP! INFO/N+", true);
  }
  
  @Override
  public String getFilter() {
    return "777";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
