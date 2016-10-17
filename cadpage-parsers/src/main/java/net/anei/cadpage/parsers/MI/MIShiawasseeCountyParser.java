package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MIShiawasseeCountyParser extends DispatchOSSIParser {
  
  public MIShiawasseeCountyParser() {
    super("SHIAWASSEE COUNTY", "MI", 
          "SRC? UNIT? CALL ADDR! ( X X? | PLACE X X? | ) CH? INFO+");
    setupSaintNames("MARYS");
  }
  
  @Override
  public String getFilter() {
    return "CAD@shiawassee.net,CAD@shiawassee.local";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{1,2}[FP]D", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z]{1,2}[FP]D\\d*(?:,[A-Z0-9]+)*");
    if (name.equals("CH")) return new ChannelField("FG ?\\d+", true);
    
    return super.getField(name);
  }
}
