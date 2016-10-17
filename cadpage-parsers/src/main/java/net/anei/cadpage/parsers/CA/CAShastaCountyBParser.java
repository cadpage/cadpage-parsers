package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchArchonixParser;


/**
 * Shasta County, CA (B)
 */
public class CAShastaCountyBParser extends DispatchArchonixParser {
  
  public CAShastaCountyBParser() {
    super(CITY_CODES, null, "SHASTA COUNTY", "CA", ARCH_FLG_OPT_CITY);
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("CN#:", "MI#:");
    return super.parseMsg(subject, body, data);
  }

  @Override
  public String getFilter() {
    return "messages@shascom.com,message@shascom911.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "RE", "REDDING"
  });
}
