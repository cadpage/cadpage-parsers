package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WASkamaniaCountyParser extends SmartAddressParser {

  public WASkamaniaCountyParser() {
    super(CITY_LIST, "SKAMANIA COUNTY", "WA");
    setFieldList("CALL ADDR APT CITY INFO CODE ID");
  }
  
  @Override
  public String getFilter() {
    return "214318";
  }
  
  private static final Pattern TRAIL_CODE_PTN = Pattern.compile("[\\.,# ]+(?:(\\d{2}-\\d{4})|(\\d{1,2}[ -][A-Z][ -]\\d{1,2}))$", Pattern.CASE_INSENSITIVE);
  private static final Pattern FIRE_EMS_PTN = Pattern.compile("[\\., ]+(FIRE|EMS)?(?: *\\bCASE)?$", Pattern.CASE_INSENSITIVE);
  private static final Pattern STATE_PTN = Pattern.compile("WA\\b[\\., ]*");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Must have a sender ID match to be considered
    if (!isPositiveId()) return false;

    // Strip off call ID and call code info from end of string
    body = body.replaceAll("  +", " ").replace(';', ',');
    while (true) {
      Matcher match = TRAIL_CODE_PTN.matcher(body);
      if (!match.find()) break;
      body = body.substring(0,match.start());
      String id = match.group(1);
      String code = match.group(2);
      if (id != null) {
        match = FIRE_EMS_PTN.matcher(body);
        if (match.find()) {
          body = body.substring(0,match.start());
          String type = match.group(1);
          if (type != null) {
            type = type.substring(0,1).toUpperCase();
            id = type + id;
          }
        }
        data.strCallId = append(id, "/", data.strCallId);
      } else {
        data.strCode = code.toUpperCase().replace(' ', '-');
      }
    }
    
    // Try to pick out an address from what is left
    // no fair using city to terminate possible address
    Result res = parseAddress(StartType.START_CALL, FLAG_NO_IMPLIED_APT | FLAG_NO_CITY, body);
    if (res.isValid()) {
      
      // Found one.  Try to pick a city name following it
      res.getData(data);
      String left = res.getLeft();
      res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, left);
      if (res.isValid()) {
        res.getData(data);
        left = res.getLeft();
        Matcher match = STATE_PTN.matcher(left);
        if (match.lookingAt()) left = left.substring(match.end());
      }
      if (data.strCall.length() == 0) {
        data.strCall = left;
      } else {
        data.strSupp = left;
      }
    }
    
    else {
      data.strSupp = body;
    }
    return true;
  }
  
  private static String[] CITY_LIST = new String[]{

    // Cities
    "STEVENSON",
    "NORTH BONNEVILLE",

    // Census designated places
    "CARSON RIVER VALLEY",

    // Other communities
    "CARSON",
    "HEMLOCK",
    "STABLER",
    "MILL A",
    "SKAMANIA",
    "UNDERWOOD",
    
    // Clark County
    "WASHOUGAL"
  };
}
