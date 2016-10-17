package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * Carter County, KY
 */
public class KYCarterCountyParser extends DispatchEmergitechParser {
  
  public KYCarterCountyParser() {
    super("Carter911:", 0, CITY_LIST, "CARTER COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "Carter911@windstream.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    return true;
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("MM")) return true;
    if (apt.endsWith("MM")) return true;
    return super.isNotExtraApt(apt);
  }

  private static final String[] CITY_LIST = new String[]{
      "CARTER",
      "GRAYSON",
      "OLIVE HILL",
      
      "ELLIOTT CO"
  };
}
