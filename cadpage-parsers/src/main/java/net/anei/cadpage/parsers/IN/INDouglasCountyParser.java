package net.anei.cadpage.parsers.IN;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Douglas County, IN
 */
public class INDouglasCountyParser extends DispatchOSSIParser {
  
  public INDouglasCountyParser() {
    super(CITY_CODES, "DOUGLAS COUNTY", "IN",
           "( CANCEL ADDR CITY! END | FYI PLACE? ADDR/Z CITY X/Z+? CALL DATETIME! END )");
  }
  
  @Override
  public String getFilter() {
    return "alerts@etieline.com";
  }
  
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() > 0 && body.startsWith("CAD:;")) {
      body = "CAD:" + subject + body.substring(3);
    }
    if (!super.parseMsg(body, data)) return false;
    
    // A city starting with a digit probably means this is a Marshall County page
    // In any case we don't want to accept it
    if (data.strCity.length() > 0 && Character.isDigit(data.strCity.charAt(0))) return false;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BOUR", "BOURBON"
  });
}
