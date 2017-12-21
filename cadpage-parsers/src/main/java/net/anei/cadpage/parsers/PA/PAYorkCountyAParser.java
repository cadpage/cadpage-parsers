package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class PAYorkCountyAParser extends SmartAddressParser {
  
  private static final Pattern STATION_PTN = Pattern.compile("\\d\\d");
  private static final Pattern DATE_TIME_PTN1 = Pattern.compile("^(\\d\\d:\\d\\d:\\d\\d)(?: +(\\d\\d-\\d\\d-\\d\\d))? +");
  private static final Pattern TIME_PTN2 = Pattern.compile(" +(\\d\\d:\\d\\d)¿?$");
  private static final Pattern ID_PTN = Pattern.compile("^(\\d{7}) ");
  private static final Pattern TIME_DATE_PTN2 = Pattern.compile("^(\\d\\d:\\d\\d:\\d\\d) +(\\d\\d-\\d\\d-\\d\\d) +");
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("^(?:([^,]+), *)?(([A-Z ]+?) +(CITY|BORO|TWP|COUNTY))\\b\\s*");
  private static final Pattern BOX_PTN = Pattern.compile("^([A-Z]+) +BOX +(\\d+)(?: [A-Z]+ +DIRECT)?  +");
  private static final Pattern MAP_PTN = Pattern.compile("\\b(\\d{2}-\\d{2,3})\\b");
  private static final Pattern DELIM = Pattern.compile("\n\n|    *");
  private static final Pattern MULTI_BLANK_PTN = Pattern.compile("  +");
  private static final Pattern TRUNC_CROSS_PTN = Pattern.compile("/ *([^ ]+) +(.*)");
  
  public PAYorkCountyAParser() {
    super("YORK COUNTY", "PA");
    setupMultiWordStreets("SEVEN VALLEYS");
    setupSpecialStreets("ALLEY");
    setupSaintNames("GEORGIA");
    removeWords("CL");
  }
  
  @Override
  public String getFilter() {
    return "FIRE@mantwp.com,paging@zoominternet.net,dtfdfilter@yahoo.com,york911alert@comcast.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() > 0) {
      if (subject.startsWith("Station ") || STATION_PTN.matcher(subject).matches()) {
        data.strSource = subject;
      }
    }
    
    Matcher match = DATE_TIME_PTN1.matcher(body);
    if (match.find()) {
      data.strTime = match.group(1);
      data.strDate = getOptGroup(match.group(2));
      body = body.substring(match.end());
    }
    
    match = TIME_PTN2.matcher(body);
    if (match.find()) {
      data.strTime = match.group(1);
      body = body.substring(0,match.start()).trim();
    }
    
    if (body.startsWith("Fire Incident / ")) {
      body = body.substring(16).trim();
    }
    
    // See if there is a leading call number
    match = ID_PTN.matcher(body);
    if (match.find()) {
      data.strCallId = match.group(1);
      body = body.substring(match.end()).trim();
    }
    
    // Rule out PAYorkCountyD messages
    String uBody = body.toUpperCase();
    if (uBody.startsWith("BOX:") || uBody.contains("CROSS STREETS:")) return false;
    
    // Check for trailing time/date
    match = TIME_DATE_PTN2.matcher(body);
    if (match.find()) {
      data.strTime = match.group(1);
      data.strDate = match.group(2).replace('-', '/');
      body = body.substring(match.end()).trim();
    }

    // There used to be a leading city followed by an address
    // Latest stand is address, city
    match = ADDR_CITY_PTN.matcher(body);
    if (!match.find()) return false;
    
    String sAddr = match.group(1);
    
    String type = match.group(4);
    if (type.equals("TWP") || type.equals("COUNTY")) {
      data.strCity = match.group(2).trim();
    } else {
      data.strCity = match.group(3);
    }
    data.strCity = convertCodes(data.strCity, CITY_ABBRVS);
    if (data.strCity.equals("CARROLL COUNTY") || data.strCity.equals("BALTIMORE COUNTY")) data.strState = "MD";
    body = body.substring(match.end()).trim();

    
    match = MAP_PTN.matcher(body);
    String part3 = "";
    if (match.find()) {
      data.strMap = match.group(1);
      part3 = body.substring(match.end()).trim();
      body = body.substring(0,match.start()).trim();
    }
    
    // Now we have to split up processing the old and new formats
    // The new format had an address in front of the city name
    if (sAddr != null) {
      setFieldList("ID BOX PLACE ADDR APT CITY ST X CALL MAP SRC UNIT TIME DATE");
      
      match = BOX_PTN.matcher(sAddr);
      if (match.find()) {
        data.strBox = match.group(1) + ' ' + match.group(2);
        sAddr = sAddr.substring(match.end());
      }
      sAddr = sAddr.replace('@', '&');
      
      // See if there is a multi blank separating place and adddress
      // If there isn't, use the smart address parser to split them apart
      match = MULTI_BLANK_PTN.matcher(sAddr);
      if (match.find()) {
        String place = sAddr.substring(0,match.start());
        sAddr = sAddr.substring(match.end());
        if (place.endsWith(sAddr)) place = place.substring(0,place.length()-sAddr.length()).trim();
        data.strPlace = place;
        
        // Check for a second multiblank separator.  If found ignore what is in front of it
        // as a duplicate city name
        match = MULTI_BLANK_PTN.matcher(sAddr);
        if (match.find()) sAddr = sAddr.substring(match.end());
        parseAddress(sAddr, data);
      } else {
        parseAddress(StartType.START_PLACE, FLAG_ANCHOR_END, sAddr, data);
      }
      
      // SO far, it is pretty simple.  There is usually a multiblank delimiter
      // Separating the cross streets from the call description
      // Except sometimes the cross street is an apt
      match = MULTI_BLANK_PTN.matcher(body);
      if (match.find()) {
        String cross = body.substring(0,match.start());
        if (cross.startsWith("APT")) {
          cross = cross.substring(3).trim();
          if (!cross.equals(data.strApt)) data.strApt = append(data.strApt, " ", cross);
        } else {
          if (!cross.equalsIgnoreCase("NO CROSS STREETS FOUND")) data.strCross = cross;
        }
        body = body.substring(match.end());
      }
      
      data.strCall = body;
    }
    
    // Older logic can get complicated
    else {
      setFieldList("TIME DATE ID CITY ST BOX PLACE ADDR APT X CALL MAP SRC UNIT");

      // Check for leading box field
      match = BOX_PTN.matcher(body);
      if (match.find()) {
        data.strBox = match.group(1) + ' ' + match.group(2);
        body = body.substring(match.end());
      }

      // Hopefully we can find a clear delimiter separating the first message piece 
      // into two parts.  If not, use the smart address parser to break up the 
      // address field
      StartType st = StartType.START_PLACE;
      match = DELIM.matcher(body);
      if (match.find()) {
        // work on the message header
        // May contain a call description followed by slash
        data.strPlace = body.substring(0,match.start()).trim();
        body = body.substring(match.end()).trim();
        st = StartType.START_ADDR;
      } 
      
      parseAddress(st, FLAG_IMPLIED_INTERSECT | FLAG_CROSS_FOLLOWS | FLAG_IGNORE_AT, body, data);
  
      // The address may be a simple address followed by a cross street
      // But the cross street may consist of a road followed by a comma 
      // followed by another cross street sequence :(
      sAddr = getLeft();
      if (sAddr.startsWith("/")) sAddr = sAddr.substring(1).trim();
      String cross = "";
      int pt = sAddr.indexOf(',');
      if (pt >= 0) {
        String tmp = sAddr.substring(0,pt).trim();
        if (isValidAddress(tmp)) {
          cross = tmp;
          sAddr = sAddr.substring(pt+1).trim();
              
        }
      }
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT, sAddr, data);
  
      // Anything left should be the call description
      // BUt check if it looks like it contains the last part of a cross street
      String left = getLeft();
      match = TRUNC_CROSS_PTN.matcher(left);
      if (match.matches()) {
        data.strCross = append(data.strCross, " / ", match.group(1));
        left = match.group(2);
      }
      data.strCall =  left;
      
      // If we don't find anything, the field we parsed as the cross street must be 
      // the call description
      if (data.strCall.length() == 0) {
        data.strCall = data.strCross;
        data.strCross = "";
      }
      data.strCross = append(cross, ", ", data.strCross);
    }
    
    // Back to common processing
    // Split what is left into source and unit fields
    boolean firstSrc = true;
    for (String term : part3.split(" +")) {
      if (term.startsWith("FIRESTA") || term.startsWith("EMSSTA")) {
        if (firstSrc) {
          data.strSource = term;
          firstSrc = false;
        } else { 
          data.strSource = append(data.strSource, " ", term);
        }
      } else {
        data.strUnit = append(data.strUnit, " ", term);
      }
    }
    
    return true;
  }
  
  private static final Properties CITY_ABBRVS = buildCodeTable(new String[]{
      "MANCH",          "MANCHESTER",
      "MANCH TWP",      "MANCHESTER TWP",
      "SHREWS TWP",     "SHREWSBURY TWP",
      "SP GARDEN TWP",  "SPRING GARDEN TWP",
      "SP GROVE",       "SPRING GROVE"
  });
}
