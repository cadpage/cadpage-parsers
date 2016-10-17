package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class MIOaklandCountyAParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("Respond to a (.*?) (?:at|-) (.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern END_MARKER = Pattern.compile(" +- +| +for +|  +", Pattern.CASE_INSENSITIVE);
  
  public MIOaklandCountyAParser() {
    super("OAKLAND COUNTY", "MI");
    setupMultiWordStreets("DOWNY NEST");
    setFieldList("CALL PLACE ADDR APT INFO");
  }
  
  @Override
  public String getFilter() {
    return "@response.troymi.gov";
  }
  
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() == 0) return false;
    
    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      data.strCall = match.group(1).trim();
      String sAddr = match.group(2).trim();
      int flags = FLAG_NO_IMPLIED_APT | FLAG_OPT_STREET_SFX;
      match = END_MARKER.matcher(sAddr);
      if (match.find()) {
        data.strSupp = sAddr.substring(match.end()).trim();
        sAddr = sAddr.substring(0,match.start()).trim();
        flags |= FLAG_ANCHOR_END;
      }
      parseAddress(StartType.START_PLACE, flags, sAddr, data);
      if (data.strSupp.length() == 0) data.strSupp = getLeft();
      if (data.strAddress.length() == 0) {
        data.strAddress = data.strPlace;
        data.strPlace = "";
      }
      else if (data.strPlace.equalsIgnoreCase("STA")) {
        data.strAddress = append (data.strPlace + " " + data.strAddress, " ", data.strSupp);
        data.strPlace = "";
        data.strSupp = "";
      }
      return true;
    }
    
    // That was our best shot.  But there are calls that fit different patterns
    if (body.startsWith("Respond to a ")) {
      body = body.substring(13).trim();
      parseAddress(StartType.START_CALL, FLAG_NO_IMPLIED_APT | FLAG_OPT_STREET_SFX, body, data);
      if (data.strCall.length() == 0) data.strCall = getLeft();
      else data.strSupp = getLeft();
      return data.strAddress.length() > 0;
    }
    
    // One last chance, but only if call has been postively IDed as a page
    if (isPositiveId()) {
      
      // Drop any possible B & C msgs
      if (body.startsWith("CAD Alert : Run ")) return false;
      if (body.startsWith("http")) return false;
      if (body.contains("https://")) return false;
      
      parseAddress(StartType.START_ADDR, FLAG_NO_IMPLIED_APT | FLAG_OPT_STREET_SFX, body, data);
      data.strCall = getLeft();
      return data.strCall.length() > 0;
    }
    
    return false;
  }
}
