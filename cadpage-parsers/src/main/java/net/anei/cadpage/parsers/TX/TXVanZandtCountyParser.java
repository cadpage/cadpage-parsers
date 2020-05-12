package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;
/**
 * Van Zandt County, TX
 */
public class TXVanZandtCountyParser extends SmartAddressParser {
  
  public TXVanZandtCountyParser() {
    super("VAN ZANDT COUNTY", "TX");
    setFieldList("CALL ADDR APT INFO");
    removeWords("ROAD");
  }
  
  public String getFilter() {
    return "DoNotReply@mcspage.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern MARKER = Pattern.compile("Van Zandt Co SO:", Pattern.CASE_INSENSITIVE);
  private static final Pattern VZCR_PTN = Pattern.compile("\\bVZ(?:CR)?(?=\\b|\\d+)", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.equals("Text Message")) break;
      
      Matcher match = MARKER.matcher(body);
      if (match.lookingAt()) {
        body = body.substring(match.end()).trim();
        break;
      }
      
      return false;
    } while (false);
    
    body = VZCR_PTN.matcher(body).replaceAll("CR");
    parseAddress(StartType.START_CALL, FLAG_IGNORE_AT | FLAG_NO_IMPLIED_APT, body, data);
    if (data.strAddress.length() == 0) {
      data.strPlace = data.strCall;
      data.strCall = "GENERAL ALERT";
      return true;
    }
    
    if (data.strCall.length() == 0) {
      data.strCall = getLeft();
    } else {
      data.strSupp = getLeft();
    }
    return true;
  }
}
