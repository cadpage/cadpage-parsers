package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class LAStJamesParishParser extends SmartAddressParser {
  
  public LAStJamesParishParser() {
    super(CITY_LIST, "ST JAMES PARISH", "LA");
    setFieldList("CALL ADDR APT CITY PLACE INFO");
  }
  
  @Override
  public String getFilter() {
    return "@stjamesla.com";
  }
  
  private static final Pattern DELIM = Pattern.compile("[;,\n]");
  private static final Pattern APT_PTN = Pattern.compile("\\d+(?:-?[A-Z])?|[A-Z]");
  private static final Pattern PLACE_PTN = Pattern.compile("MANRESA|.* CHURCH", Pattern.CASE_INSENSITIVE);
  private static final Pattern MASTER1 = Pattern.compile("(.*?) at (?:(.*) located )?(.*?) in (CONVENT|GRAMERCY|LUTCHER|NORTH VACHERIE|PAULINA|SOUTH VACHERIE|ST JAMES|WELCOME)\\b *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern MASTER2 = Pattern.compile("(.*?) at (.*?)", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Not distinctive features - rely on positive ID
    if (!isPositiveId()) return false;
    
    // If there is a subject, it always seems to be a call description
    data.strCall = subject;
    
    // See if message is broken up by field delimiters
    String[] flds = DELIM.split(body);
    if (flds.length > 1) {
      
      for (String part : flds) {
        part = part.trim();
        if (part.length() == 0) continue;
        
        // First pieces is always the address
        if (data.strAddress.length() == 0) {
          parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, part, data);
          continue;
        }
        
        // See if it looks like a city
        if (isCity(part)) {
          data.strCity = part;
          continue;
        }
        
        // Or an apt
        if (APT_PTN.matcher(part).matches()) {
          data.strApt = append(data.strApt, "-", part);
          continue;
        }
        
        // Or a place name (really flakey here)
        if (PLACE_PTN.matcher(part).matches()) {
          data.strPlace = append(data.strPlace, " - ", part);
          continue;
        }
        
        // Grab call if we don't already have fone
        if (data.strCall.length() == 0) {
          data.strCall = part;
          continue;
        }
        
        if (part.equals(data.strCall)) continue;
        
        // Otherwise append to info
        data.strSupp = append(data.strSupp, "\n", part);
      }
      
      // Call description appended to end of address :(
      data.strAddress = stripFieldEnd(data.strAddress, data.strCall);
      return true;
      
    }
    
    // Try to match a known pattern
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      data.strCall = append(data.strCall, " - ", match.group(1).trim());
      data.strPlace = getOptGroup(match.group(2));
      parseAddress(match.group(3).trim(), data);
      data.strCity = match.group(4);
      data.strSupp =  stripFieldEnd(match.group(5).trim(), ".");
      return true;
    }
    
    match = MASTER2.matcher(body);
    if (match.matches()) {
      data.strCall = append(data.strCall, " - ", match.group(1).trim());
      parseAddress(match.group(2).trim(), data);
      return true;
      
    }
    
    if (data.strCall.length() > 0) {
      parseAddress(body, data);
      return true;
    }
    
    return false;
  }
  
  private static final String[] CITY_LIST = new String[]{
    "CONVENT",
    "GRAMERCY",
    "LUTCHER",
    "NORTH VACHERIE",
    "PAULINA",
    "SOUTH VACHERIE",
    "ST JAMES",
    "WELCOME"
  };

}
