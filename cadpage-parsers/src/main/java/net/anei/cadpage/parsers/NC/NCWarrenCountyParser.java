
package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCWarrenCountyParser extends DispatchSouthernParser {

  public NCWarrenCountyParser() {
    super(CITY_LIST, "WARREN COUNTY", "NC", 
          DSFLG_ADDR|DSFLG_OPT_CODE|DSFLG_ID|DSFLG_TIME);
  }
  
  @Override
  public String getFilter() {
    return "@warrencountync.com";
  }
  
  private static final Pattern DISPATCH_PTN = Pattern.compile(" +[A-Za-z]+\\d+$");
  private static final Pattern MASTER2 = Pattern.compile("(.*?) (20\\d{3,}) (.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher match = DISPATCH_PTN.matcher(body);
    if (match.find()) body = body.substring(0, match.start());
    
    if (!super.parseMsg(body, data)) return false;
    if (data.strSupp.length() > 0) {
      data.strCall = data.strCall + data.strSupp;
      data.strSupp = "";
    }
    return true;
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
