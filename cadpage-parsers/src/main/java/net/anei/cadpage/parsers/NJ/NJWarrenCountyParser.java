package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


/**
 * Warren County, NJ
 */
public class NJWarrenCountyParser extends SmartAddressParser {
  
  private static final Pattern DATE_TIME_PTN1 = Pattern.compile(" +\\[([ 0-9]\\d/\\d\\d) ([ 0-9]\\d{3})\\]$");
  private static final Pattern DATE_TIME_PTN2 = Pattern.compile(" +\\(SENT (\\d\\d/\\d\\d) (\\d\\d:\\d\\d)\\)$");
  private static final Pattern REGULAR_MARKER = Pattern.compile("([A-Z0-9]+) (?:(\\d{8}) )?ALERT: +");
  private static final Pattern HAZMAT_MASTER = Pattern.compile("([A-Z0-9]+ HAZMAT) (?:(\\d{8}) )?L-\\d RESPONSE REQUESTED: *(.*?) AT ([^,/]+)(?:/([^,]+))?,([^,]+),(.+)");
  private static final Pattern DISREGARD_MASTER = Pattern.compile("([A-Z0-9]+) (DISREGARD ALERT FOR [ A-Z]+) AT +(.*?)(?:,(.*))?");
  private static final Pattern DELIM = Pattern.compile(" *, +");
  private static final Pattern CITY_TRIM_PTN = Pattern.compile(" (?:BORO|TOWN)$");
  
  public NJWarrenCountyParser() {
    super(CITY_LIST, "WARREN COUNTY", "NJ");
    setFieldList("UNIT ID CALL PLACE ADDR APT INFO CITY DATE TIME INFO X");
    setupMultiWordStreets(MWORD_STREET_LIST);
    removeWords("HEIGHTS", "MALL");
  }
  
  @Override
  public String getFilter() {
    return "WC911@co.warren.nj.us,root@co.warren.nj.us";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    String[] lines = body.split("\n");
    body = lines[0];
    Matcher match = DATE_TIME_PTN1.matcher(body);
    if (!match.find()) {
      match = DATE_TIME_PTN2.matcher(body);
      if (!match.find()) return false;
    }
    data.strDate = match.group(1);
    String time = match.group(2);
    if (time.length() == 4) time = time.substring(0,2).trim() + ':' + time.substring(2,4);
    data.strTime = time;
    body = body.substring(0,match.start());
    
    // There are two calls, regular and hazmat, with slightly different formats
    // Let's handle the regular one first
    match = REGULAR_MARKER.matcher(body);
    if (match.lookingAt()) {
      
      data.strUnit = match.group(1);
      data.strCallId = getOptGroup(match.group(2));
      String sAddr = body.substring(match.end());
      
      // Sometimes there are nice comma delimiters
      String[] flds = DELIM.split(sAddr);
      if (flds.length == 3) {
        data.strCall = flds[0];
        parseAddressField(flds[1], data);
        data.strCity = flds[2];
      }
      
      // and sometimes there are not
      else {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, sAddr, data);
      }
      
      // Clean up odd things in call and city fields
      if (data.strCall.endsWith("/")) data.strCall = data.strCall.substring(0,data.strCall.length()-1).trim();
      match = CITY_TRIM_PTN.matcher(data.strCity);
      if (match.find()) data.strCity = data.strCity.substring(0,match.start());
      
      if (lines.length > 1) {
        String cross = lines[1];
        if (cross.startsWith("Cross Streets-")) {
          data.strCross = cross.substring(14).trim();
        }
      }
      return true;
    }
    
    // Check for a disregard alert notice
    match = DISREGARD_MASTER.matcher(body);
    if (match.matches()) {
      data.strUnit = match.group(1);
      data.strCall = match.group(2);
      parseAddress(match.group(3), data);
      data.strCity = getOptGroup(match.group(4));
      return true;
    }
    
    // No such luck, see if this has a Hazmat marker
    match = HAZMAT_MASTER.matcher(body);
    if (match.matches()) {
      data.strUnit = match.group(1);
      data.strCallId = getOptGroup(match.group(2));
      data.strCall = match.group(3).trim();
      String tmp = match.group(4).trim();
      if (isValidAddress(tmp)) {
        parseAddress(tmp, data);
      } else {
        data.strPlace = tmp;
      }
      tmp = getOptGroup(match.group(5));
      if (tmp.length() > 0) {
        if (data.strAddress.length() == 0 && isValidAddress(tmp)) {
          parseAddress(tmp, data);
        } else {
          data.strPlace = append(data.strPlace, " / ", tmp);
        }
      }
      
      data.strSupp = match.group(7).trim();
      tmp = match.group(6).trim();
      if (isCity(tmp)) {
        data.strCity = tmp;
      } else {
        data.strSupp = append(tmp, " / ", data.strSupp);
      }
      return true;
    }
    
    // Strike out
    return false;
  }

  private void parseAddressField(String field, Data data) {
    String[] parts = field.split(" / ");
    for (int j = 0; j<parts.length; j++) {
      parts[j] = parts[j].trim();
    }
    
    int lastPart = parts.length-1;
    while (lastPart > 0 && parts[lastPart].startsWith("APT ")) lastPart--;
    int status = 0;
    
    for (int j = 0; j<parts.length; j++) {
      String part = parts[j];
      parts[j] = part;
      if (part.length() == 0) continue;
      if (j+1<parts.length && part.equals(parts[j+1])) continue;
      
      if (part.startsWith("APT ")) {
        data.strApt = append(data.strApt, "-", part.substring(4).trim());
        continue;
      }
      
      switch (status) {
      case 0:
        if (j == lastPart || isValidAddress(part) || NUMERIC.matcher(part).matches()) {
          parseAddress(part, data);
          status = 1;
        } else {
          data.strPlace = append(data.strPlace, " - ", part);
        }
        break;
        
      case 1:
        if (isValidAddress(part) || NUMERIC.matcher(part).matches()) {
          parseAddress(part, data);
          break;
        }
        status = 2;
        
      case 2:
        data.strSupp = append(data.strSupp, " ", part);
        break;
      }
    }
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BALD EAGLE",
    "BUCKELEY HILL",
    "HARMONY BRASS CASTLE",
    "HOPE BLAIRSTOWN",
    "HOPE BRIDGEVILLE",
    "HOPE GREAT MEADOW",
    "JENNY JUMP",
    "LAKE JUST IT",
    "LOCUST LAKE",
    "MT HERMON",
    "MT PISGAH",
    "PINCHERS POINT",
    "RED SCHOOL",
    "SADDLE RIDGE",
    "SPRING VALLEY",
    "STATE PARK"
  };
  
  private static final String[] CITY_LIST = new String[]{
    "ALLAMUCHY TWP",
    "ALLAMUCHY-PANTHER VALLEY",
    "ALPHA BORO",
    "BELVIDERE TOWN",
    "BEATYESTOWN",
    "BLAIRSTOWN TWP",
    "BRASS CASTLE",
    "FRANKLIN TWP",
    "FRELINGHUYSEN TWP",
    "GREAT MEADOWS-VIENNA",
    "GREENWICH TWP",
    "HACKETTSTOWN TOWN",
    "HARDWICK TWP",
    "HARMONY TWP",
    "HOPE TWP",
    "INDEPENDENCE TWP",
    "KNOWLTON TWP",
    "LIBERTY TWP",
    "LOPATCONG TWP",
    "MANSFIELD TWP",
    "OXFORD",
    "OXFORD TWP",
    "PHILLIPSBURG TOWN",
    "POHATCONG TWP",
    "PORT MURRAY",
    "WASHINGTON BORO",
    "WASHINGTON TWP",
    "WHITE TWP",
  };
}
