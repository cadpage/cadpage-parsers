package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MIShiawasseeCountyParser extends DispatchOSSIParser {
  
  public MIShiawasseeCountyParser() {
    super("SHIAWASSEE COUNTY", "MI", 
          "SRC? CALL ADDR! X+? SRC? INFO/N+? ( DATETIME UNIT | UNIT ) UNIT/C+");
    setupSaintNames("MARYS");
  }
  
  @Override
  public String getFilter() {
    return "CAD@shiawassee.net,CAD@shiawassee.local";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{1,2}[FP]D", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new UnitField("(?:\\b[A-Z]{3,4}\\d*\\b,?)+");
    
    return super.getField(name);
  }
}
