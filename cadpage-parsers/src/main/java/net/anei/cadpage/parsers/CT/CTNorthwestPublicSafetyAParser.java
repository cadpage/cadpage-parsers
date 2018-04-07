package net.anei.cadpage.parsers.CT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Roxbury, CT
 * Seymour, CT
 */
public class CTNorthwestPublicSafetyAParser extends SmartAddressParser {
  
  private static final Pattern ID_PTN = Pattern.compile("(.*?)\\bPrimary Incident: *(?:(\\d{2}-?\\d{3,4})\\b *)?(.*)");
  private static final Pattern UNIT_PTN = Pattern.compile("\\b(?:ROX?|RX|TANGO)\\b");
  private static final Pattern APT_EXT_PTN = Pattern.compile("APT|UNIT|ROOM|RM|(LOT|SUITE|FLR)", Pattern.CASE_INSENSITIVE);

  public CTNorthwestPublicSafetyAParser() {
    super(CITY_LIST, "", "CT");
    setFieldList("ADDR APT PLACE CITY CALL CODE UNIT ID DATE TIME");
  }
  
  @Override
  public String getLocName() {
    return "Northwest Public Safety, CT";
  }

  @Override
  public String getFilter() {
    return "globalpaging@nowestps.org,no-reply@nowestps.org,NWCTPS@nowestps.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("From Northwest")) return false;
    body = body.replace('\n', ' ');
    Matcher match = ID_PTN.matcher(body);
    if (!match.matches()) return false;
    String sAddr = match.group(1).trim();
    data.strCallId = getOptGroup(match.group(2));
    String sAddr2 = match.group(3);
    
    if (sAddr2.startsWith("Call Received Time:")) {
      Parser p = new Parser(sAddr2.substring(19).trim());
      data.strDate = p.get(' ');
      data.strTime = p.get(' ');
      sAddr2 = p.get();
    }
    
    if (sAddr.length() == 0) {
      sAddr = sAddr2;
      sAddr2 = "";
    }
    
    parseAddress(StartType.START_ADDR, FLAG_PAD_FIELD, sAddr, data);
    data.strPlace = getPadField();
    if (data.strApt.startsWith("(")) {
      int pt = data.strPlace.indexOf(')');
      if (pt >= 0) {
        data.strApt = data.strApt + ' ' + data.strPlace.substring(0,pt+1);
        data.strPlace = data.strPlace.substring(pt+1).trim();
      }
    } 
    else if ((match = APT_EXT_PTN.matcher(data.strApt)).matches()) {
      Parser p = new Parser(data.strPlace);
      data.strApt = append(getOptGroup(match.group(1)), " ", p.get(' '));
      data.strPlace = p.get();
    }
    else if (data.strApt.equals("PARKING") && data.strPlace.startsWith("LOT")) {
      data.strApt = "PARKING LOT";
      data.strPlace = data.strPlace.substring(3).trim();
    }
    else if (data.strApt.length() == 0 && data.strPlace.startsWith("&")) {
      data.strAddress = data.strAddress + " " + data.strPlace;
      data.strPlace = "";
    }
    
    data.strPlace = stripFieldStart(data.strPlace, "-");
    if (isValidAddress(data.strPlace)) {
      data.strAddress = append(data.strAddress, " ", '(' + data.strPlace + ')');
      data.strPlace = "";
    }
    
    body = getLeft();
    match = UNIT_PTN.matcher(body);
    if (match.find()) {
      data.strUnit = body.substring(match.start());
      body = body.substring(0,match.start()).trim();
    }
    int pt = body.indexOf(" ReferenceText:");
    if (pt >= 0) {
      data.strCode = body.substring(pt+15).trim();
      body = body.substring(0,pt).trim();
    }
    data.strCall = body;
    
    // There is a Primary Incident address at the end of the call that
    // is usually the same as the initial address.  When it is different
    // it is the address that we should use, unless it is just a truncated
    // version of the initial address
    if (!sAddr.startsWith(sAddr2)) {
      data.strAddress = "";
      data.strApt = "";
      parseAddress(StartType.START_ADDR, sAddr2, data);
    }
    return true;
  }
  
  private static String[] CITY_LIST = new String[]{
    "ANSONIA",
    "BRIDGEWATER",
    "NEW MILFORD",
    "ROXBURY",
    "SEYMOUR",
    "SHELTON",
    "SOUTHBURY",
    "WASHINGTON"
  };
}
