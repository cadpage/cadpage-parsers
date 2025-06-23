package net.anei.cadpage.parsers.GA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class GAPierceCountyAParser extends SmartAddressParser {

  public GAPierceCountyAParser() {
    super(CITY_LIST, "PIERCE COUNTY", "GA");
    setFieldList("ID CODE DATE TIME CALL PHONE ADDR APT CITY GPS");
    setupMultiWordStreets(MWORD_STREETS);

  }

  @Override
  public String getFilter() {
    return "pierce.ga@ez911map.net";
  }

  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("SOP \\d+ \\d{4}-\\d-\\d+");
  private static final Pattern MASTER = Pattern.compile("(\\d{2}-\\d{2}[A-Z]?|S\\d+) (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M) +");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private static final Pattern GPS_PTN = Pattern.compile("(.*?) {2,}http://.*\\?q=(.*)");
  private static final Pattern PHONE_PTN = Pattern.compile(" +(\\d{3} \\d{3}-?\\d{4}) +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = subject;

    match = MASTER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCode =  match.group(1);
    data.strDate = match.group(2);
    setTime(TIME_FMT, match.group(3), data);
    body = body.substring(match.end());

    match = GPS_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1);
      setGPSLoc(match.group(2), data);
    }

    match = PHONE_PTN.matcher(body);
    if (match.find()) {
      data.strCall = body.substring(0, match.start());
      data.strPhone = match.group(1);
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, body.substring(match.end()), data);
    } else {
      parseAddress(StartType.START_CALL, FLAG_ANCHOR_END,  body, data);
    }

    return true;
  }

  private static final String[] MWORD_STREETS = new String[] {
      "BLUE LAKE",
      "BOB BOWEN",
      "MIDWAY CHURCH",
      "MT PLEASANT",
      "OAK RIDGE",
      "RIVER OAKS",
      "SATILLA BLUFF",
      "ST JOHNS CHURCH",
      "SUNNY MEADOWS",
      "TYRE BRIDGE"
  };

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BLACKSHEAR",
      "OFFERMAN",
      "PATTERSON",

      // Unincorporated communities
      "BRISTOL",
      "MERSHON",
      "OTTER CREEK",
      "WALKERVILLE",

      // Ware County
      "WAYCROSS"
  };
}
