package net.anei.cadpage.parsers.MS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchBParser;



public class MSMarionCountyParser extends DispatchBParser {
  
  private static final Pattern MARKER = Pattern.compile("^(?:-  - )?911-CENTER:");
  private static final Pattern CUTOFF_PTN = Pattern.compile("\\bCUT +OFF\\b", Pattern.CASE_INSENSITIVE);

  public MSMarionCountyParser() {
    super(CITY_LIST, "MARION COUNTY", "MS");
    setupMultiWordStreets(
        "MT CARMEL CHURCH",
        "NEW HOPE KOKOMO",
        "SPRING HILL CHURCH",
        "STRINGER BULLOCK",
        "TAYLORS CUT OFF",
        "TEN MILE CREEK"
    );
  }
  
  @Override
  public String getFilter() {
    return "@co.marion.ms.us";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    body = body.substring(match.end());
    if (! super.parseMsg(body, data)) return false;
    return true;
  }

  @Override
  protected boolean isPageMsg(String body) {
    return true;
  }

  @Override
  protected boolean parseAddrField(String field, Data data) {
    field = CUTOFF_PTN.matcher(field).replaceAll("CUTOFF");
    return super.parseAddrField(field, data);
  }

  private static final String[] CITY_LIST = new String[]{
    "COLUMBIA",
    
    "BUNKER HILL",
    "CHERAW",
    "EAST COLUMBIA",
    "FOXWORTH",
    "GOSS",
    "HUB",
    "IMPROVE",
    "KOKOMO",
    "MORGANTOWN",
    "SANDY HOOK",
    "JAMESTOWN"
  };
}
