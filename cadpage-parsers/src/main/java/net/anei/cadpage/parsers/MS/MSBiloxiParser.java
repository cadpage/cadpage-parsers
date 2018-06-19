package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA37Parser;

import java.util.regex.*;

public class MSBiloxiParser extends DispatchA37Parser {

  public MSBiloxiParser() {
    super("BiloxiDispatch", "BILOXI", "MS");
    setupMultiWordStreets("RUE MAISON",
                          "JIM BYRD",
                          "POPPS FERRY",
                          "CEDAR LAKE",
                          "JOHN LEE",
                          "FATHER RYAN",
                          "COVENENT SQUARE",
                          "JAMES MADISON");
    addCrossStreetNames("KEESLER");
  }
  
  @Override
  public String getFilter() { 
    return "BiloxiDispatch@biloxi.ms.us,Biloxi_Dispatch@biloxi.ms.us"; 
  }
  
  @Override
  protected boolean parseLocationField(String field, Data data) {
    
    // Trim a BLOCK OF construct so something we know how to handle
    field = field.replace(" BLOCK OF ", " BLK ");
    field = field.replace('@', '&');
    
    // Remove dash from interstate highways
    field = I_DASH_PTN.matcher(field).replaceAll(" ");

    String addressField;
    Matcher m = LOCATION_PATTERN.matcher(field);
    if (!m.matches()) return false;   // Never happens
    addressField = m.group(1).trim();
    parseAddress(addressField, data);
    data.strPlace = append(data.strPlace, " - ", getOptGroup(m.group(2)).trim());
    data.strPhone = getOptGroup(m.group(3)).trim();
    return true;
  }
  private static final Pattern I_DASH_PTN = Pattern.compile("(?<=I)-(?=\\d)");
  private static final Pattern LOCATION_PATTERN
  = Pattern.compile("([^-]*)(?:\\-(.*?)(?:\\s*((?:\\d{3}\\s*\\-\\s*\\d{4}\\D*)*)))?");
  
  @Override
  protected boolean parseMessageField(String field, Data data) {
    
    // Trim a BLOCK OF construct so something we know how to handle
    field = field.replace(" BLOCK OF ", " BLK ");
    field = field.replace('@', '&');
    
    // Check for ID address unit format
    Matcher match = ID_UNIT_PTN.matcher(field);
    if (match.matches()) {
      parseAddressField(match.group(1), data);
      data.strUnit = match.group(2);
      return true;
    }
    
    // Trim off leading call ID.  We already have a call ID from the main parser
    // so we just discard this one
    match = ID_PTN.matcher(field);
    if (match.matches()) field = match.group(1);
    
    // Check for slash addr slash call pattern
    match = SLASH_ADDR_PTN.matcher(field);
    if (match.matches()) {
      parseAddress(StartType.START_ADDR, FLAG_AT_SIGN_ONLY, match.group(1).trim(), data);
      data.strPlace = getLeft();
      appendCall(match.group(2).trim(), data);
      return true;
    }
    
    // Trim off responding unit from front
    match = UNIT_PTN.matcher(field);
    if (match.matches()) {
      String unit = match.group(1);
      if (unit == null) unit = match.group(3);
      if (unit != null) data.strUnit = unit;
      field = match.group(2);
    }
    
    // A FOR A clause designates the end of an address
    match = FOR_PTN.matcher(field);
    if (match.matches()) {
      String addr = match.group(1);
      String left = match.group(2);
      int pt = addr.indexOf('/');
      if (pt >= 0) {
        parseAddress(StartType.START_ADDR, FLAG_AT_SIGN_ONLY | FLAG_ANCHOR_END | FLAG_CHECK_STATUS, addr.substring(0,pt).trim(), data);
        data.strPlace = addr.substring(pt+1).trim();
      } else {
        Result res = parseAddress(StartType.START_PLACE, FLAG_AT_SIGN_ONLY, addr);
        if (res.isValid()) {
          res.getData(data);
          data.strPlace = stripFieldEnd(data.strPlace, " AT");
          String place = res.getLeft();
          if (place.startsWith("&") || place.startsWith("/")) {
            data.strAddress = data.strAddress + " &" + place.substring(1);
          } else {
            data.strPlace = append(data.strPlace, " - ", place);
          }
        } else {
          parseAddress(addr, data);
        }
      }
      if (left != null) appendCall(left, data);
      return true;
    }
    
    // If we did not get a call description from the main message
    parseAddressField(field, data);
    
    return true;
  }
  
  private static final Pattern ID_UNIT_PTN = Pattern.compile("(?:INC )?\\d{4} +(.*?)[ /]+((?:[^ /]+ +AND +)?[^ /]+) +(?:RESPONDING|RESPONDED|ENROUTE)", Pattern.CASE_INSENSITIVE);
  private static final Pattern ID_PTN = Pattern.compile("(?:(?:\\d{2}-\\d+|INC \\d{4}|\\d{4}(?= +\\d+ )) +)?(.*?)(?:[ /]+(?:INCIDENT|INCID#) +\\d+)?");
  private static final Pattern SLASH_ADDR_PTN = Pattern.compile("/(.*?)/(.*)");
  private static final Pattern UNIT_PTN = Pattern.compile("([^ ]+|STA \\d+) +RESP(?:ONDING)? +(?:INCIDENT +[-\\d]+ +)?(?:TO +)?(.*?)", Pattern.CASE_INSENSITIVE);
  private static final Pattern FOR_PTN = Pattern.compile("(.*?) +FOR +(?:A +)?(?:MEDICAL|(.*))", Pattern.CASE_INSENSITIVE);

  private void parseAddressField(String addr, Data data) {
    int pt = addr.indexOf('/');
    if (pt >= 0) {
      parseAddress(StartType.START_ADDR, FLAG_AT_SIGN_ONLY | FLAG_OPT_STREET_SFX, addr.substring(0,pt).trim(), data);
      data.strPlace = getLeft();
      appendCall(addr.substring(pt+1).trim(), data);
    } else {
      Result res = parseAddress(StartType.START_OTHER, FLAG_AT_SIGN_ONLY | FLAG_OPT_STREET_SFX | FLAG_AND_NOT_CONNECTOR, addr);
      if (!res.isValid()) {
        data.strSupp = addr;
      } else {
        res.getData(data);
        if (data.strAddress.equalsIgnoreCase("TO WALK")) {
          data.strAddress = "";
          data.strSupp = addr;
        } else {
          appendCall(res.getStart(), data);
          appendCall(res.getLeft(), data);
        }
      }
    }
  }
  
  private void appendCall(String field, Data data) {
    if (data.strCall.length() + field.length() > 57) {
      data.strSupp = append(data.strSupp, " / ", field);
    } else {
      data.strCall = append(data.strCall, " / ", field);
    }
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " PLACE ADDR APT PHONE INFO UNIT";
  }
}
