package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOFarmingtonParser extends FieldProgramParser {

  public MOFarmingtonParser() {
    super("FARMINGTON", "MO",
          "CALL ADDR/S! X INFO/N+");
    removeWords("STE");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@FARMINGTON.MO";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // There just are not enough identifying characteristics to positively identify this
    // as a dispatch alert
    if (!isPositiveId()) return false;
    return parseFields(body.split("\n"), data);
  }
}
