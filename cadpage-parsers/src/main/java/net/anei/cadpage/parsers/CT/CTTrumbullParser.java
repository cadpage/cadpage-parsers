package net.anei.cadpage.parsers.CT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class CTTrumbullParser extends SmartAddressParser {
  
  private static final Set<String>STATION_CODES = 
    new HashSet<String>(Arrays.asList(new String[]{"TC", "LH", "N"}));

  private static final Pattern PIPE_PTN = Pattern.compile(" *\\| *");
  private static final Pattern CHANNEL_PTN = Pattern.compile("\\b(FC(?: ?\\d)?|-) +");
  private static final Pattern TONE_PTN = Pattern.compile("^((?:[A-Z0-9]+ +)?\\d[A-Z]{2,3} TONE) ");
  private static final Pattern LEAD_PLACE_PTN = Pattern.compile("^((?:IN )?AREA OF|IFO|IAO) ");
  private static final Pattern TRAIL_PLACE_PTN = Pattern.compile("^(.* (?:CLUBHOUSE|BUS STOP|CTR|CENTER|BLDG|BUILDING|SCHOOL|ASSOCIATES)) ");
  
  public CTTrumbullParser() {
    super(CITY_LIST, "TRUMBULL", "CT");
    setFieldList("SRC UNIT CH PLACE ADDR APT CITY CALL");
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Remove the Spotted Dog field separators.  We will see if we can do better
    body = PIPE_PTN.matcher(body).replaceAll(" ");
    int pt = body.indexOf(' ');
    if (pt < 0) return false;
    String src = body.substring(0,pt).trim();
    body = body.substring(pt+1).trim();
    for (String code : src.split("/")) {
      if (! STATION_CODES.contains(code)) return false;
    }
    data.strSource = src;
    
    Matcher match = CHANNEL_PTN.matcher(body);
    if (!match.find()) return false;
    pt = match.end();
    data.strUnit = body.substring(0, match.start()).trim();
    if (data.strUnit.startsWith("LADDER COVERAGE")) {
      data.strCall = data.strUnit;
      data.strUnit = "";
    }
    data.strChannel = match.group(1);
    if (data.strChannel.startsWith("LADDER COVERAGE")) {
      data.strCall = data.strChannel;
      data.strUnit = "";
    }
    if (data.strChannel.equals("-")) data.strChannel = "";
    body = body.substring(match.end()).trim();
    
    match = LEAD_PLACE_PTN.matcher(body);
    if (match.find()) {
      data.strPlace = match.group(1);
      body = body.substring(match.end()).trim();
    }
    
    match = TONE_PTN.matcher(body);
    if (match.find()) {
      data.strCall = append(match.group(1), " - ", data.strCall);
      body = body.substring(match.end()).trim();
      
      match = LEAD_PLACE_PTN.matcher(body);
      if (match.find()) {
        data.strPlace = match.group(1);
        body = body.substring(match.end()).trim();
      }
    }
    
    parseAddress(StartType.START_ADDR, FLAG_NO_IMPLIED_APT  | FLAG_CROSS_FOLLOWS, body, data);
    body = getLeft();
    
    if (body.endsWith(" F/A")) {
      data.strPlace = append(data.strPlace, " ", body.substring(0,body.length()-4).trim());
      body = "FIRE ALARM";
    } else {
      match = TRAIL_PLACE_PTN.matcher(body);
      if (match.find()) {
        data.strPlace = append(data.strPlace, " ", match.group(1));
        body = body.substring(match.end()).trim();
      } else {
        if (body.equals("F/A")) body = "FIRE ALARM";
      }
    }
    
    data.strCall = append(data.strCall, " - ", body);
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return CL_PTN.matcher(addr).replaceAll("CIR");
  }
  private static final  Pattern CL_PTN = Pattern.compile("\\bCL\\b");
  
  private static final String[] CITY_LIST = new String[]{
    "TRUMBULL"
  };
}
