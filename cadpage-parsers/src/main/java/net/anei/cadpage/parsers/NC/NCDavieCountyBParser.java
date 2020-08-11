package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCDavieCountyBParser extends MsgParser {

  public NCDavieCountyBParser() {
    super("DAVIE COUNTY", "NC");
    setFieldList("CALL ADDR CITY ST APT NAME DATE TIME ID INFO UNIT CH");
  }

  @Override
  public String getFilter() {
    return "no-reply@daviecountync.gov";
  }

  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile(";? +(\\d{1,4}[A-Z]{0,2}|[A-Z]{2,5}\\d+)$");
  private static final Pattern DATE_TIME_ID_PTN = Pattern.compile("(.*) (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) (\\d{11}) (.*)");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");
  private static final Pattern ADDR_CITY_ST_PTN = Pattern.compile("([^,]+), *([A-Z ]*)(?:, *([A-Z]{2}) +(?:(\\d{5}) +)?|\\b +)");
  private static final Pattern APT_PTN = Pattern.compile("\\b(?:APT|RM|ROOM|ROM|ER)\\b");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    data.strCall = subject;

    // Strip unit and channel information from end of alert
    Matcher match = TRAIL_UNIT_PTN.matcher(body);
    if (match.find()) {
      do {
        String unit = match.group(1);
        if (unit.startsWith("TAC")) {
          data.strChannel = append(unit, ",", data.strChannel);
        } else {
          data.strUnit = append(unit, ",", data.strUnit);
        }
        body = body.substring(0,match.start()).trim();
        if (!match.group().startsWith(";")) break;
        match = TRAIL_UNIT_PTN.matcher(body);
      } while (match.find());

    } else {
      return false;
    }

    // Look for the date/time/id identifier that separates main information from the info
    match = DATE_TIME_ID_PTN.matcher(body);
    if (!match.matches()) return false;
    body = match.group(1).trim();
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    data.strCallId = match.group(4);
    String info = match.group(5).trim();

    // Process the information blocks
    info = stripFieldStart(info, "None");
    for (String part : INFO_BRK_PTN.split(info)) {
      part = part.trim();
      data.strSupp = append(data.strSupp, "\n", part);
    }

    // Getting there.  Things get easier if we can identify a city/state/zip combination
    match = ADDR_CITY_ST_PTN.matcher(body);
    if (match.lookingAt()) {
      parseAddress(match.group(1).trim(), data);
      data.strCity =  match.group(2);
      data.strState = getOptGroup(match.group(3));
      if (data.strCity.length() == 0) data.strCity = getOptGroup(match.group(4));
      body = body.substring(match.end()).trim();

      // Grab the apartment and name from what is left
      parseAptName(body, data);
    }

    // Rather than use the smart address parser, we will look for a apartment identifier.
    // Since calls without cities are almost always intersections with no apartment, they
    // almost always are terminated by "None".  But we will look for a regular apartment
    // identifier, just in case.
    else {
      int pt = body.indexOf(" None ");
      if (pt < 0) {
        match = APT_PTN.matcher(body);
        if (match.find()) pt = match.start();
      }
      if (pt >= 0) {
        parseAptName(body.substring(pt).trim(), data);
        body = body.substring(0,pt).trim();
      }
      parseAddress(body, data);
    }
    return true;
  }

  private void parseAptName(String body, Data data) {
    Matcher match;
    if (body.startsWith("None ")) {
      String name = body.substring(5).trim();
      if (!name.equals("None")) {
        data.strName = cleanWirelessCarrier(name);
      }
    }  else if (body.endsWith(" None")) {
      String apt = body.substring(0, body.length()-5).trim();
      match = APT_PTN.matcher(apt);
      if (match.lookingAt()) apt = apt.substring(match.end()).trim();
      data.strApt= append(data.strApt, "-", apt);
    }
  }
}
