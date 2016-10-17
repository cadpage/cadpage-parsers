package net.anei.cadpage.parsers.SD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class SDMinnehahaCountyParser extends SmartAddressParser {
 
  public SDMinnehahaCountyParser() {
    super(CITY_CODES, "MINNEHAHA COUNTY", "SD");
    setFieldList("SRC UNIT MAP ADDR APT PLACE UNIT CITY CALL CODE ID");
  }
  
  @Override
  public String getFilter() {
    return "911metrodispatch@911metro.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final String MAP_PTN_STR = "\\b(Baltic \\d{1,2}|Brandon[- ]\\d{1,2}|Colton City|Colton \\d{1,2}|Crooks \\d{1,2}|Dell Rapids \\d|Garretson \\d{1,2}|Hartford \\d{1,2}|Humboldt \\d{1,2}|Lyons \\d{1,2}|Quad [A-Z0-9]{4}(?:_\\d+)?|Renner \\d{1,2}|Splitrock \\d{1,2}|Valley Springs \\d{1,2})\\b";
  private static final Pattern CAD_MSG_PTN = 
    Pattern.compile("(?:((?:[A-Z]{1,2}\\d* +)+))?(?:(\\d{3}) +)?(?:((?:[A-Z]{2} +)+))?(?:(Quad [A-Z0-9]{3,4}) - ([A-Z]{2})|(\\d{4}-\\d{8}(?:, *\\d{4}-\\d{8})*)|" + MAP_PTN_STR + ")? *\\b(.+?)(?: (C\\d))?(?: (\\d{4}-\\d{8}))?");
  private static final Pattern STREET_NO_ADDR_PTN = Pattern.compile("\\d+ (?!ST\\b|AVE?\\b).*");
  private static final Pattern DISPATCH_MSG_PTN = 
      Pattern.compile("(.*?) +(\\d{4}-\\d{8})((?:  Dispatch received by unit ([^ ]+))+)");
  
  private static final Pattern LEAD_MAP_PTN = Pattern.compile('^' + MAP_PTN_STR);
  private static final Pattern TRAIL_MAP_PTN = Pattern.compile(MAP_PTN_STR + '$');
  private static final Pattern MM_PTN = Pattern.compile("( MM \\d+)([^\\d ])");
  private static final Pattern MM_PTN2 = Pattern.compile("^MM \\d+");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = CAD_MSG_PTN.matcher(body);
    if (match.matches()) {
      data.strUnit = append(getOptGroup(match.group(1)), " ", getOptGroup(match.group(3)));
      data.strSource = getOptGroup(match.group(2));
      data.strMap = getOptGroup(match.group(4));
      String sCityCode = getOptGroup(match.group(5));
      data.strCallId = getOptGroup(match.group(6));
      if (data.strMap.length() == 0) data.strMap = getOptGroup(match.group(7));
      String sAddrFld = match.group(8);
      if (data.strSource.length() > 0 && data.strMap.length() == 0 && 
          sCityCode.length() == 0 && data.strCallId.length() == 0 && 
          !STREET_NO_ADDR_PTN.matcher(sAddrFld).matches()) {
        sAddrFld = append(data.strSource, " ", sAddrFld);
        data.strSource = "";
      }
      data.strCode = getOptGroup(match.group(9));
      String id = match.group(10);
      if (id != null) data.strCallId = id;
      
      // Dispatch never puts a blank between mile markers and city codes :(
      sAddrFld = MM_PTN.matcher(sAddrFld).replaceFirst("$1 $2");
      
      // Whose bright idea was it to use DR as a city code?
      pt = -1;
      String pad;
      if (sCityCode.equals("DR")) pt = sAddrFld.lastIndexOf(" DR ");
      if (pt >= 0) {
        parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_ALLOW_DUAL_DIRECTIONS, 
                     sAddrFld.substring(0,pt).trim(), data);
        pad = getLeft();
        data.strCall = sAddrFld.substring(pt+4).trim();
        data.strCity = "DELL RAPIDS";
      } else {
        parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_ALLOW_DUAL_DIRECTIONS | FLAG_PAD_FIELD, sAddrFld, data);
        pad = getPadField();
        data.strCall = getLeft();
      }

      match = MM_PTN2.matcher(data.strCall);
      if (match.find()) {
        data.strAddress = append(data.strAddress, " ", match.group());
        data.strCall = data.strCall.substring(match.end()).trim();
      }
      
      if (data.strCity.length() == 0) {
        match = CITY_PTN.matcher(data.strCall);
        if (match.matches()) {
          pad = match.group(1);
          String city = match.group(2);
          data.strCall = match.group(3);
          if (city.equals("DR")) {
            data.strCity = "DELL RAPIDS";
          } else {
            data.strCity = convertCodes(city, CITY_CODES);
          }
        }
      }

      if (data.strMap.length() == 0) {
        match = TRAIL_MAP_PTN.matcher(pad);
        if (match.find()) {
          data.strMap = match.group(1);
          pad = pad.substring(0,match.start()).trim();
        }
        else if ((match = LEAD_MAP_PTN.matcher(data.strCall)).lookingAt()) {
          data.strMap = match.group(1);
          data.strCall = data.strCall.substring(match.end()).trim();
        }
      }
      if (pad.length() <= 4) {
        data.strApt = append(data.strApt, "-", pad);
      } else if (pad.startsWith("MM ")) {
        data.strAddress = append(data.strAddress, " ", pad);
      } else {
        data.strPlace = pad;
      }
      
      // Almost everything is optional, but we have to have some standards
      // If we don't have a map or a call ID, reject this
      if (data.strMap.length() == 0 && data.strCallId.length() == 0) return false;
      
      return true;
    }
    
    else if ((match = DISPATCH_MSG_PTN.matcher(body)).matches()) {
      String addrFld = match.group(1);
      data.strCallId = match.group(2);
      data.strUnit = match.group(3).replace("Dispatch received by ", "").trim();
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_ALLOW_DUAL_DIRECTIONS, addrFld, data);
      data.strCall = getLeft();
      return data.strCall.length() > 0;
    }
    else return false;
  }
  
  private static final Pattern CITY_PTN = Pattern.compile("(.*?) *(BA|BR|CO|CR|DR|GA|GN|HD|HU|LY|RE|VS|SR|EM) +(.*)");
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BA", "BALTIC",
      "BR", "BRANDON",
      "CO", "COLTON",
      "CR", "CROOKS",
//      "DR", "DELL RAPIDS",  // gets confused drive DR road suffix :(
      "GA", "GARRETSON",
      "GN", "GARRETSON",
      "HD", "", // Should be Hartford but actually in sioux falls
      "HU", "HUMBOLT",
      "LY", "LYONS",
      "RE", "RENNER",
      "SF", "SIOUX FALLS",
      "SH", "SHERMAN",
      "VS", "VALLEY SPRINGS",
      "SR", "SPLIT ROCK",
      "EM", ""
  });
}
