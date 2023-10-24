package net.anei.cadpage.parsers.LA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * E Feliciana Parish, LA
 */
public class LAEastFelicianaParishParser extends SmartAddressParser {


  public LAEastFelicianaParishParser() {
    super(CITY_LIST, "EAST FELICIANA PARISH", "LA");
    setFieldList("ID CALL DATE TIME ADDR APT PLACE CITY ST");
  }

  @Override
  public String getFilter() {
    return "EASTFELICIANA911@PAGINGPTS.COM";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD Autopage EventID:(\\d{10})");
  private static final Pattern MASTER_PTN = Pattern.compile("Call (\\d{10}) ([A-Z0-9]+) (\\d\\d?/\\d\\d?/\\d{4}), (\\d\\d?:\\d\\d [AP]M) {2,}(.*?)(?:, +([A-Z]{2}))?(?: +(\\d{5}))?");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);

    match = MASTER_PTN.matcher(body);
    if (!match.matches()) return false;
    if (!match.group(1).equals(data.strCallId)) return false;
    data.strCall =  match.group(2);
    data.strDate = match.group(3);
    setTime(TIME_FMT, match.group(4), data);
    String addr = match.group(5);
    data.strState = getOptGroup(match.group(6));
    String zip = match.group(7);

    addr = stripFieldEnd(addr, " EF");
    parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
    if (data.strApt.equals("ST") || data.strApt.equals("LN")) data.strApt = "";
    if (data.strCity.isEmpty() && zip !=  null) data.strCity = zip;

    return true;
  }

  private static final String[] CITY_LIST = new String[]{

      // Towns
      "CLINTON",
      "JACKSON",
      "SLAUGHTER",

      // Villages
      "NORWOOD",
      "WILSON",

      // Unincorporated communities
      "BLUFF CREEK",
      "ETHEL",
      "GURLEY",

      // East Baton Rouge Parish
      "ZACHARY",

      // West Feliciana Parish
      "ST FRANCESVILLE",
      "ST FRANCISVILLE"
  };
}
