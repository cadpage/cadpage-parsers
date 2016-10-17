package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Wayne County, OH
 */
public class OHWayneCountyAParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("taccad:(.*)");
  
  public OHWayneCountyAParser() {
    super("WAYNE COUNTY", "OH");
    setFieldList("CALL ADDR APT INFO");
  }
  
  @Override
  public String getFilter() {
    return "@pd.rittman.com,taccad";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    do {
      Matcher match = MASTER.matcher(body);
      if (match.matches()) {
        body = match.group(1).trim();
        break;
      }
      
      // Anything starting with (Dispatch Message) should go to the B parser
      if (subject.equals("Dispatch Message")) return false;
      
      // Anything starting with Dispatch: should go to the C parser
      if (body.startsWith("Dispatch:")) return false;
      
      // Anything starting with CALL: should go to the D parser
      if (body.contains("CALL:")) return false;
      
      // We'll take it as long as caller identified this as a dispatch msg :(
      if (isPositiveId()) break; 
      return false;
      
    } while (false);
    
    parseAddress(StartType.START_CALL, 
                 FLAG_START_FLD_REQ | FLAG_IMPLIED_INTERSECT | FLAG_NO_IMPLIED_APT, 
                 body, data);
    String sPlace = getLeft();
    data.strSupp = sPlace;
    if (data.strAddress.length() >= 4 ) return true;
    return data.parseGeneralAlert(this, body);
  }

  
}
