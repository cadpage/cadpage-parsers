
package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCWarrenCountyParser extends DispatchSouthernParser {
  
  private static final Pattern MASTER2 = Pattern.compile("(.*?) (20\\d{3,}) (.*)");

  public NCWarrenCountyParser() {
    super(CITY_LIST, "WARREN COUNTY", "NC", DSFLAG_FOLLOW_CALL);
  }
  
  @Override
  public String getFilter() {
    return "@warrencountync.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (super.parseMsg(body, data)) return true;
    
    
    // Check for alternate fallback format missing a date.time
    Matcher match = MASTER2.matcher(body);
    setFieldList("CALL ID ADDR APT CITY");
    if (!match.matches()) return false;

    data.initialize(this);
    data.strCall = match.group(1).trim();
    data.strCallId = match.group(2);
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(3).trim(), data);
    return data.strCity.length() > 0;
  }
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.endsWith(" MM")) return true;
    return super.isNotExtraApt(apt);
  }

  private static final String[] CITY_LIST = new String[]{
    
    "ALBERTA",
    "AURELIAN SPRINGS",
    "BOYDTON",
    "BRINKLEYVILLE",
    "BRODNAX",
    "BRODNAX",
    "BUNN",
    "CENTERVILLE",
    "CHASE CITY",
    "CLARKSVILLE",
    "CONWAY",
    "ENFIELD",
    "FRANKLINTON",
    "GARYSBURG",
    "GASBURG",
    "GASTON",
    "HALIFAX",
    "HEATHSVILLE",
    "HENDERSON",
    "HOBGOOD",
    "HOLLISTER",
    "JACKSON",
    "KITTRELL",
    "LA CROSSE",
    "LASKER",
    "LAWRENCEVILLE",
    "LITTLETON",
    "LOUISBURG",
    "MACON",
    "MANSON",
    "MIDDLEBURG",
    "NORLINA",
    "RICH SQUARE",
    "ROANOKE RAPIDS",
    "SCOTLAND NECK",
    "SEABOARD",
    "SEVERN",
    "SOUTH HENDERSON",
    "SOUTH HILL",
    "SOUTH ROSEMARY",
    "SOUTH WELDON",
    "WAKE FOREST",
    "WARRENTON",
    "WELDON",
    "WHITE PLAINS",
    "WISE",
    "WOODLAND",
    "YOUNGSVILLE",
  };
}
