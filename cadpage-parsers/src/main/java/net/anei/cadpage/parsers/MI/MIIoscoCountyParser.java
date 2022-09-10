package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

/**
 * Iosco County, MI
 */
public class MIIoscoCountyParser extends DispatchA19Parser {
  
  
  public MIIoscoCountyParser() {
    super("IOSCO COUNTY", "MI");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "noreply@iosco911.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.endsWith(" Townshi")) data.strCity += 'p';
    return true;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "6690 N F 41",                          "+44.475743,-83.388967",
      "3191 N M 65",                          "+44.369041,-83.805047",
      "5155 N US 23",                        "+44.430301,-83.330577",
      "5226 N US 23",                        "+44.431831,-83.329657",
      "5665 N US 23",                        "+44.444239,-83.330572",
      "5939 N US 23",                        "+44.452649,-83.329287",
      "3140 S M 65",                          "+44.186485,-83.804062",
      "3999 S M 65",                          "+44.162127,-83.805766",
      "1504 S US 23",                        "+44.422680,-83.330044",
      "1970 S US 23",                        "+44.422784,-83.329937",
      "2344 S US 23",                        "+44.422836,-83.330001"
  });
}
