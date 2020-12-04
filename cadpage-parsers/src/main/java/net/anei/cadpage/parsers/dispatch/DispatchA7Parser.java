package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA7Parser extends DispatchA7BaseParser {

  // Bit flag indicating that the city code found in the address line
  // is not really a city code and should be treated as part of the map code
  public static final int A7_FLG_ADDR_MAP_CODE = 1;

  int flags = 0;
  private Properties cityCodes = null;
  private Set<String> citySet = null;
  private boolean passThrough = false;

  public DispatchA7Parser(Set<String> citySet, Properties cityCodes, String defCity, String defState, int flags) {
    super(defCity, defState, null);
    this.citySet = citySet;
    this.cityCodes = cityCodes;
    this.flags = flags;
  }

  public DispatchA7Parser(int initCityIndex, String[] cityIndex, Properties cityCodes,
                          String defCity, String defState, String program) {
    super(initCityIndex, cityIndex, cityCodes, defCity, defState, program);
    passThrough = (program != null);
    this.cityCodes = cityCodes;
  }

  private static final Pattern SOFT_BREAK_PTN = Pattern.compile("\n(?=[^/ ])");

  private static final Pattern ID_PTN = Pattern.compile("\\bInc(?:ident)? History(?: for)?:? #([A-Z]+\\d+)\\b.*\n");
  private static final Pattern STATE_DATE_TIME_PTN = Pattern.compile("([A-Z][a-z]+)(?:  +(\\d\\d/\\d\\d/\\d\\d) +(\\d\\d:\\d\\d:\\d\\d)\\b.*)?");

  private static final Pattern CALL_PTN1 = Pattern.compile("Initial Type: [^ ]+ +Final Type: ([^ ]+) +\\((.*)\\)");
  private static final Pattern PRI_PTN1 = Pattern.compile("Initial Priority: [A-Z0-9] +Final Priority: ([A-Z0-9])");
  private static final Pattern CALL_PRI_PTN2 = Pattern.compile("Final Type: ([^ ]+) *\\((.*?)\\) +Pri: ([A-Z0-9])\\b.*");
  private static final Pattern PRIMARY_UNIT_PTN = Pattern.compile(".* Primary Unit:(.*?)( +Rsp:.*)?");

  private static final Pattern BOX_PTN1 = Pattern.compile("Police BOX: *[^ ]* +Fire BOX: *([^ ]*) +EMS BOX: *([^ ]*)");
  private static final Pattern BOX_PTN2 = Pattern.compile("Police +BLK: [^ ]* +Fire +BLK: *([^ ]*)");
  private static final Pattern BOX_PTN2B = Pattern.compile("EMS +BLK: ([^ ]*) +DSP +BLK: ?[^ ]*");
  private static final Pattern BOX_PTN3 = Pattern.compile("EMS Blk: *([^ ]*) +Fire Blk: *([^ ]*) +Police Blk: *[^ ]* +Map Page: *([^ ]*)");
  private static final Pattern MAP_LAT_LONG_PTN1 = Pattern.compile("Group: (.*?) +Section: (\\S*) +Map: (\\S*) +X: *(\\d*) +Y: *(\\d*)");
  private static final Pattern MAP_LAT_LONG_PTN2 = Pattern.compile("Group: (.*?) +Beat: (\\S*) +Map Page: (\\S*) +Lat: +([-+][\\.0-9]+)? Long: ?([-+][\\.0-9]+)?");
  private static final Pattern ADDR_CROSS_PTN = Pattern.compile("Loc: *(.*?) *(?:high xst: (.*?) *)?");

  private static final Pattern COMMUNITY_PTN = Pattern.compile("Community: +(.*?)");
  private static final Pattern MUNICIPALITY_PTN = Pattern.compile("AKA: *(.*?) +Municipality: +(.*?) +Dev:.*");
  private static final Pattern LOC_INFO_PTN = Pattern.compile("Loc Info: *(.*?)");
  private static final Pattern LOC_INFO_SPLIT_PTN = Pattern.compile(" *(?:,|---+) *");
  private static final Pattern LOC_INFO_APT_PTN = Pattern.compile("(?:RM|UNIT|SUITE?|STE|APT|BLDG) +.*|.* +(?:FLOOR)",Pattern.CASE_INSENSITIVE);
  private static final Pattern LOC_INFO_APT2_PTN = Pattern.compile("(?:RM|APT|SUITE?) +#? *(.*?)");
  private static final Pattern LOC_INFO_PHONE_PTN = Pattern.compile("#?(\\d{3}[\\.-]?\\d{3}[\\.-]?\\d{4})");
  private static final Pattern LOC_INFO_CROSS_PTN = Pattern.compile("(?:XSTS?|BTWN)[- :] *(.*)");
  private static final Pattern NAME_ADDR_PHONE_PTN = Pattern.compile("Name: *(.*?) +(?:ADDR|Addr): (.*?) +(?:PH|Phone): *(.*?)");
  private static final Pattern NAME_PHONE_PTN = Pattern.compile("Name: *(.*?) +CC: .*? +Phone: *(.*?)");
  private static final Pattern ADDR_PTN = Pattern.compile("Addr: *(.*?)");

  private static final Pattern ENTRY_MARK_PTN = Pattern.compile("/\\d++\\?? +(?:\\([A-Z0-9 ]+\\) +)?[*$]?([A-Z]+):?(?: {6,}(.*?) *| {1,5}([A-Z0-9]+)\\b.*)?");
  private static final Pattern ENTRY_GPS_PTN = Pattern.compile("([-+]\\d{3}\\.\\d{6,} +[-+]\\d{3}\\.\\d{6,})\\b[ ,]*");
  private static final Pattern CONT_MARK_PTN = Pattern.compile(" {30,}(.*?) *");

  @Override
  protected boolean parseMsg(String body, Data data) {

    // Bucks County has a problem.   They have a base county parser class that sometimes wants to use our
    // parser, and sometimes does not.  We solve this by allowing them to pass a program string to the
    // superclass constructor, and if that program string is not null, we just invoke the superclass
    // parser
    if (passThrough) return super.parseMsg(body, data);

    setFieldList("SRC ID DATE TIME CODE CALL PRI UNIT BOX MAP GPS ADDR APT CITY ST X PLACE NAME PHONE INFO");

    // They insert their own wraparound soft breaks, which we need to remove
    // Except on one very unique page, they don't.  So we check for that as well
    if (!body.contains("\nDispatched")) {
      body = SOFT_BREAK_PTN.matcher(body).replaceAll(" ");
    }

    // We have to find an ID pattern
    Parser p = new Parser(body);
    Matcher match =  p.getMatcher(ID_PTN);
    if (match ==  null) return false;
    data.strCallId = match.group(1);

    // Process the state/date/time block
    // Extract dispatch date and time for Received, Entered, and Dispatched lines
    // If any other line is found with a date and time, turn this into a run report
    // On further reflection, we will let lines witih enroute and onscene times be
    // reported as dispatch alerts.  It doesn't make sense, but these do seem to
    // be the first and only alerts that go out for most calls ?????
    boolean runReport = false;
    match = getNextMatchLine(p, STATE_DATE_TIME_PTN);
    if (match == null) return false;
    String line = "";
    while (true) {
      String state = match.group(1);
      String date = match.group(2);
      String time = match.group(3);
      if (state.equals("Entered") || state.equals("Received") ||  state.equals("Dispatched")) {
        if (date != null) {
          data.strDate = date;
          data.strTime = time;
        }
      } else {
//        if (date != null) runReport = true;
      }
      line = p.getLine();
      if (line == null) return false;
      match = STATE_DATE_TIME_PTN.matcher(line);
      if (!match.matches()) break;
    }
    if (runReport) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      match = getNextMatchLine(p, PRIMARY_UNIT_PTN);
      if (match != null) data.strUnit = match.group(1).trim();
      return true;
    }

    // Skip over any blank lines
    while (line.length() == 0) {
      line = p .getLine();
      if (line.length() == 0) return false;
    }

    // There are two variations on the next block of information
    match = CALL_PTN1.matcher(line);
    if (match.matches()) {
      data.strCode = match.group(1);
      data.strCall = match.group(2).trim();

      match = getMatchLine(p, PRI_PTN1);
      if (match == null) return false;
      data.strPriority = match.group(1);

      // We don't process alarm levels
      p.getLine();

      // Retrieve unit field from disposition line
      match = getMatchLine(p, PRIMARY_UNIT_PTN);
      if (match == null) return false;
      data.strUnit = match.group(1).trim();
    }

    // Process the second variant of call priority information
    else {
      match = getMatchLine(p, CALL_PRI_PTN2);
      if (match == null) return false;
      data.strCode = match.group(1);
      data.strCall = match.group(2).trim();
      data.strPriority = match.group(3);
    }

    // In either case, strip trailing asterisk from call description
    if (data.strCall.endsWith("*")) {
      data.strCall = data.strCall.substring(0,data.strCall.length()-1).trim();
    }

    // And there are a couple variations of the box, map, and GPS information
    line = p.getLine();
    if (line == null) return false;
    if ((match = BOX_PTN1.matcher(line)).matches()) {
      parseBox(match.group(1), match.group(2), data);

      match = getMatchLine(p, MAP_LAT_LONG_PTN1);
      if (match == null) return false;
      parseMapLatLongLine(match, data, true);
    }

    else if ((match = BOX_PTN2.matcher(line)).matches()) {
      String fireBox = match.group(1);
      line = p.getLine();
      if (line == null) return false;
      match = BOX_PTN2B.matcher(line);
      if (!match.matches()) return false;
      parseBox(fireBox, match.group(1), data);

      line = skipBlankLines(p);
      if (line == null) return false;
      match = MAP_LAT_LONG_PTN2.matcher(line);
      if (!match.matches()) return false;
      parseMapLatLongLine(match, data, false);
    }

    else if ((match = BOX_PTN3.matcher(line)).matches()) {
      parseBox(match.group(2), match.group(1), data);
      data.strMap = match.group(3);
      p.getLine();
    }

    // Time to process the address line
    line = skipBlankLines(p);
    if (line == null) return false;
    match = ADDR_CROSS_PTN.matcher(line);
    if (!match.matches()) return false;
    parseAddressA7(match.group(1).trim(), data);
    data.strCross = append(data.strCross, " & ", getOptGroup(match.group(2)));

    // See if the city code found here should be treated as a map code
    if ((flags & A7_FLG_ADDR_MAP_CODE) != 0) {
      data.strMap = append(data.strCity, "-", data.strMap);
      data.strCity = "";
    }

    // Now things start to get optional
    line = p.getLine();
    if (line == null) return false;
    if (line.length() == 0 || line.equals("(V)") || line.equals("(NV)")) {
      line = p.getLine();
      if (line == null)  return false;
    }

    if (line.startsWith("Intr Info:")) {
      line = p.getLine();
      if (line == null) return false;
    }

    // Process community line
    match = COMMUNITY_PTN.matcher(line);
    if (match.matches()) {
      String place = match.group(1);
      if (citySet != null && citySet.contains(place)) {
        data.strCity = place;
      } else if (!data.strPlace.contains(place)) {
        data.strPlace = append(place, " - ", data.strPlace);
      }
      line = p.getLine();
      if (line == null) return false;
    }

    match = MUNICIPALITY_PTN.matcher(line);
    if (match.matches()) {
      data.strPlace = append(data.strPlace, " - ", match.group(1).trim());
      String city = match.group(2);
      if (city.length() > 0) data.strCity = city;
      line = p.getLine();
      if (line == null) return false;
    }

    match = LOC_INFO_PTN.matcher(line);
    if (match.matches()) {
      String place = "";
      for (String tmp : LOC_INFO_SPLIT_PTN.split(match.group(1))) {
        tmp = tmp.trim();
        if (tmp.length() == 0) continue;
        if (citySet != null && citySet.contains(tmp)) {
          data.strCity = tmp;
          continue;
        }
        if (data.strCross.length() == 0 && (match = LOC_INFO_CROSS_PTN.matcher(tmp)).matches()) {
          data.strCross = append(data.strCross, " & ", match.group(1));
          continue;
        }
        if ((match = LOC_INFO_PHONE_PTN.matcher(tmp)).matches()) {
          data.strPhone = append(data.strPhone, " / ", match.group(1));
          continue;
        }
        if (LOC_INFO_APT_PTN.matcher(tmp).matches()) {
          String tmp2 = tmp;
          match = LOC_INFO_APT2_PTN.matcher(tmp2);
          if (match.matches()) tmp2 = match.group(1);
          if (tmp2.length() <= 10) {
            data.strApt = tmp2;
            continue;
          }
        }
        if (isValidCrossStreet(tmp)) {
          data.strCross = append(data.strCross, " & ", tmp);
          continue;
        }
        if (data.strPlace.contains(tmp)) continue;
        place = append(place, " ", tmp);
      }
      data.strPlace = append(data.strPlace, " - ", place);
      line = p.getLine();
      if (line == null) return false;
    }

    // If we got a city, do some extra processing on it
    if (data.strCity.length() > 0) {
      String city = data.strCity;
      if (cityCodes != null) city = convertCodes(city, cityCodes);
      int pt = city.indexOf(',');
      if (pt >= 0) {
        data.strState = city.substring(pt+1).trim();
        city = city.substring(0,pt).trim();
      }
      data.strCity = city;
    }

    // Two different versions of caller name/address/phone
    match = NAME_ADDR_PHONE_PTN.matcher(line);
    if (match.matches()) {
      data.strName = cleanWirelessCarrier(match.group(1).trim());
      parseAddrLine(match.group(2).trim(), data);
      data.strPhone = append(data.strPhone, " / ", match.group(3).trim());
    }

    else if ((match = NAME_PHONE_PTN.matcher(line)).matches()) {
      data.strName = cleanWirelessCarrier(match.group(1).trim());
      data.strPhone = append(data.strPhone, " / ", match.group(2).trim());

      line = p.getLine();
      if (line == null) return false;
      match = ADDR_PTN.matcher(line);
      if (!match.matches()) return false;
      parseAddrLine(match.group(1).trim(), data);
    }

    else return false;

    //  Start processing unit history lines
    boolean entry = false;
    String priUnit = data.strUnit;
    while (true) {
      line = p.getUntrimmedLine();
      if (line == null) break;
      if (line.trim().length() == 0) continue;
      if (line.length() < 23) break;
      if ((match = ENTRY_MARK_PTN.matcher(line)).matches()) {
        String type = match.group(1);
        String text = match.group(2);
        String unit = match.group(3);
        entry = false;
        if (text != null) {
          entry = type.equals("ENTRY") || type.equals("COPY") || type.equals("SUPP");
          if  (entry) {
            text = stripFieldStart(text, "TXT:");
            match = ENTRY_GPS_PTN.matcher(text);
            if (match.lookingAt()) {
              setGPSLoc(match.group(1), data);
              text = text.substring(match.end());
            }
            if (!text.startsWith("PROQA")) data.strSupp = append(data.strSupp, "\n", text);
          }
        }
        else if (unit != null && !unit.equals(priUnit)) {
          if (type.startsWith("DISP") || type.startsWith("ASST") || type.equals("ADD")) {
            data.strUnit = append(data.strUnit, " ", unit);
          }
        }
      }

      else if ((match = CONT_MARK_PTN.matcher(line)).matches()) {
        String text = match.group(1);
        if (entry) {
          if (text.startsWith("TXT:")) text = text.substring(4).trim();
          data.strSupp = append(data.strSupp, " ", text);
        }
      }
      else break;
    }

    return true;
  }

  private String skipBlankLines(Parser p) {
    while (true) {
      String line = p.getLine();
      if (line ==  null) return null;
      if (line.length() > 0) return line;
    }
  }

  /***
   * Read line line from parser and match against pattern
   * @param p parser
   * @param pattern pattern
   * @return pattern match object if successful, null otherwise
   */
  private Matcher getMatchLine(Parser p, Pattern pattern) {
    String line =  p.getLine();
    if (line == null) return null;
    Matcher match = pattern.matcher(line);
    if (!match.matches()) return null;
    return match;
  }

  /**
   * Read through a parser buffer a line at a time, looking for a line
   * that matches the input string
   * @param p Parser
   * @param pattern pattern being searched for
   * @return A match object if a line match is found, otherwise null
   */
  private Matcher getNextMatchLine(Parser p, Pattern pattern) {
    while (true) {
      String line = p.getLine();
      if (line == null) return null;
      Matcher match = pattern.matcher(line);
      if (match.matches()) return match;
    }
  }

  private void parseBox(String fireBox, String emsBox, Data data) {
    fireBox = fireBox.trim();
    emsBox = emsBox.trim();
    if (fireBox.equals(emsBox)) {
      data.strBox = fireBox;
    }
    else {
      if (fireBox.length() > 0) fireBox = "F:" + fireBox;
      if (emsBox.length() > 0) emsBox = "E:" + emsBox;
      data.strBox = append(fireBox, " ", emsBox);
    }
  }

  private void parseMapLatLongLine(Matcher match, Data data, boolean xy) {
    data.strMap = match.group(3);

    String lat = match.group(4);
    String lon = match.group(5);
    if (!isValidCoord(lat) || !isValidCoord(lon)) return;
    if (xy) {
      setGPSLoc(fixCoord(lon) + ',' + fixCoord(lat), data);
    } else {
      setGPSLoc(lat + ',' + lon, data);
    }
  }

  private boolean isValidCoord(String coord) {
    if (coord == null) return false;
    if (coord.length() == 0) return false;
    if (coord.equals("0000000")) return false;
    if (coord.length() < 7) return false;
    return true;
  }

  private String fixCoord(String coord) {
    int insPt = coord.length()-5;
    return coord.substring(0,insPt) + '.' + coord.substring(insPt);
  }

  private void parseAddrLine(String sAddr, Data data) {
    if (sAddr.length() > 0 && !sAddr.equals(data.strAddress)) {
      data.strName = append(data.strName, " ", "(" + sAddr + ")");
    }
  }

}
