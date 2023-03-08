package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCWayneCountyDParser extends DispatchOSSIParser {

  public NCWayneCountyDParser() {
    super(CITY_CODES, "WAYNE COUNTY", "NC", 
          "( CANCEL ADDR CITY " +
          "| CALL ADDR ID CITY UNIT EMPTY GPS1 GPS2 CH! " + 
          ") INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "DUD", "DUDLEY",
      "EUR", "EUREKA",
      "FOR", "FOUR OAKS",
      "FRE", "FREMONT",
      "GLB", "GOLDSBORO",
      "LAG", "LA GRANGE",
      "MTO", "MOUNT OLIVE",
      "PRN", "PRINCETON",
      "PIK", "PIKEVILLE",
      "SSP", "SEVEN SPRINGS",
      "STA", "STANTONSBURG"
  });
}
