package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class COGilpinCountyParser extends DispatchOSSIParser {
  
  public COGilpinCountyParser() {
    super("GILPIN COUNTY", "CO", 
          "FYI SRC? CALL ADDR! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@co.gilpin.co.us";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{1,3}FD", true); 
    return super.getField(name);
  }

}
