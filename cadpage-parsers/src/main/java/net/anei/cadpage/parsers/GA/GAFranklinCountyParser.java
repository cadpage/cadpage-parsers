package net.anei.cadpage.parsers.GA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * Franklin County, GA
 */
public class GAFranklinCountyParser extends DispatchSouthernParser {
  
  public GAFranklinCountyParser() {
    super(CITY_LIST, "FRANKLIN COUNTY", "GA", 
          DSFLAG_DISPATCH_ID | DSFLAG_ID_OPTIONAL | DSFLAG_FOLLOW_CROSS);
  }
  
  @Override
  public String getFilter() {
    return "FC911@franklincountyga.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("FC911:")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (NOT_APT_PTN.matcher(apt).matches()) return true;
    return super.isNotExtraApt(apt);
  }
  private static final Pattern NOT_APT_PTN = Pattern.compile("I[- ].*", Pattern.CASE_INSENSITIVE);




  private static final String[] CITY_LIST = new String[]{
    "CANON",
    "CARNESVILLE",
    "FRANKLIN SPRINGS",
    "GUMLOG",
    "LAVONIA",
    "MARTIN",
    "ROYSTON",
    
    // Jackson County
    "COMMERCE"
  };

}
