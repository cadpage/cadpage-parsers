package net.anei.cadpage.parsers.GA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class GAJeffersonCountyParser extends SmartAddressParser {

  public GAJeffersonCountyParser() {
      super(CITY_LIST, "JEFFERSON COUNTY", "GA");
      setupMultiWordStreets(MWORD_STREETS);
      setFieldList("ID CALL DATE TIME PLACE ADDR APT CITY GPS");
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("[A-Z]{3} \\d{4}-\\d+");
  private static final Pattern MASTER = Pattern.compile("([-/A-Z]+) (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M) +(.*?)( +https?://maps.google.com.*\\?q=(.*))?");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private static final Pattern PLACE_ADDR_PTN = Pattern.compile("(.*) (?://)? (.*)");

  public boolean parseMsg(String subject, String body, Data data) {

    if (!SUBJECT_PTN.matcher(subject).matches()) return false;
    data.strCallId = subject;

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1);
    data.strDate = match.group(2);
    setTime(TIME_FMT, match.group(3), data);
    body = match.group(4);
    String gps = match.group(5);
    if (gps != null) setGPSLoc(gps, data);

    match = PLACE_ADDR_PTN.matcher(body);
    if (match.matches()) {
      String place =  match.group(1).trim();
      if (!place.equals("HWY")) {
        data.strPlace = place;
        body = match.group(2).trim();
      }
    }

    body = body.replace('@', '&');
    parseAddress(StartType.START_ADDR, body, data);

    // Ignore anything trailing the first city as a duplicate address

    // Which may have been confused as a trailing address
    if (data.strAddress.startsWith(data.strApt))data.strApt = "";

    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "AVERA",
      "LOUISVILLE",
      "N WRENS",
      "STAPLETON",
      "WADLEY",
      "WRENS",

      // Towns
      "BARTOW",

      // Census-designated place
      "MATTHEWS"
  };

  private static String[] MWORD_STREETS = new String[]{
    "CLARKS MILL",
    "EDEN CHURCH",
    "GAMBLE SCHOOL",
    "GEORGE WILLIAMS",
    "LINCOLN PARK",
    "MOSLEY CHAPEL",
    "MOXLEY BARTOW",
    "NOAH STATION",
    "OAK GROVE",
    "OLD STAPLETON",
    "SAND VALLEY",
    "WILLIAMS BRIDGE"
  };
}
