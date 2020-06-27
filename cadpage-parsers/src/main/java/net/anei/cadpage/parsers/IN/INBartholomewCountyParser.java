package net.anei.cadpage.parsers.IN;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Bartholomew County, IN
 */
public class INBartholomewCountyParser extends DispatchOSSIParser {
  
  public INBartholomewCountyParser() {
    super(CITY_CODES, "BARTHOLOMEW COUNTY", "IN",
           "FYI ( DATETIME CALL ADDR CITY!  X+ | CALL ADDR CITY! X/Z+? DATETIME )");
  }
  
  @Override
  public String getFilter() {
    return "bcpaging@bartholomew.in.gov,8123418140";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    boolean good = false;
    int pt = body.lastIndexOf('-');
    if (pt >= 0 && "- File Messenger Login".startsWith(body.substring(pt))) {
      good = true;
      body = body.substring(0,pt).trim();
    }
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    if (super.parseMsg(body, data)) return true;
    if (!good) return false;
    return data.parseGeneralAlert(this, body.substring(4).trim());
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CLIF", "CLIFFORD",
      "COL",  "COLUMBUS",
      "HOPE", "HOPE",
      "TAYL", "TAYLORSVILLE"
  });
}
