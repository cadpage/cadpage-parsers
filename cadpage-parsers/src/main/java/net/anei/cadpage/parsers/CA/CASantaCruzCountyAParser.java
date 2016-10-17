package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;

/**
 * Santa Cruz County, CA
 */
public class CASantaCruzCountyAParser extends DispatchPrintrakParser {
  
  private static final Pattern MASTER_FIX1_PTN = Pattern.compile("([^ ]{3}) (\\d) ([A-Z]{3}\\d{12}) ([^ ]+(?: [EF])?) +(.*)");
  
  public CASantaCruzCountyAParser() {
    super("SANTA CRUZ COUNTY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "SCR911_CAD@scr911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
 
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // SOmeone is occasionally truncating the normal labels :(
    Matcher match = MASTER_FIX1_PTN.matcher(body);
    if (match.matches()) body = match.group(1) + " PRI: " + match.group(2) + " INC: " + match.group(3) + " TYP:" + match.group(4) + " AD: " + match.group(5);
    if (!super.parseMsg(body, data)) return false;
    data.strSource = "";
    
    // Place sometimes duplicates address
    if (data.strPlace.equals(data.strAddress)) data.strPlace = "";
    
    // And some just plain wrong city names
    else if (data.strCity.equals("AP/LA SELVA")) data.strCity = "APTOS";
    
    return true;
  }
  
  private static final Pattern CITY_AREA_PTN = Pattern.compile("BIG REDWOOD PRK|MISSION SPRINGS|PASATIEMPO|ZAYANTE|.* AREA?|.* AR|.* BYPASS|.* CORR?IDOR");
  
  @Override
  public String adjustMapCity(String city) {
    
    // And there are a lot of city names that describe broad areas rather than 
    // something Google recognizes.  
    if (CITY_AREA_PTN.matcher(city).matches()) return "";
    return city;
  }
}
