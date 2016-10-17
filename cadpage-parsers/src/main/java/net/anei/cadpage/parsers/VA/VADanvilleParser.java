package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class VADanvilleParser extends DispatchOSSIParser {
  
  public VADanvilleParser() {
    super("DANVILLE", "VA",
        // A complicated way to do CALL PLACE? ADDR X X
          "CALL ( PLACE ADDR/Z X/Z X/Z ENDMARK " +
               "| ADDR! X X END " +
               "| ADDR/Z X! X END " +
               "| ADDR/Z! END " +
               "| PLACE ADDR! X X END )");
  }
  
  @Override
  public String getFilter() {
    return "CAD@danvilleva.gov";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      Result res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, field);
      if (res.getStatus() != STATUS_STREET_NAME) return false;
      res.getData(data);
      return true;
    }
  }
}
