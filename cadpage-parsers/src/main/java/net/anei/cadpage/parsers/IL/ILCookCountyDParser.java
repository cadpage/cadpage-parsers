package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class ILCookCountyDParser extends MsgParser {

  public ILCookCountyDParser() {
    super("COOK COUNTY", "IL");
    setFieldList("CALL TIME UNIT ADDR APT CITY PLACE INFO ID GPS MAP");
  }

  @Override
  public String getFilter() {
    return "911@nwcds.org";
  }

  private static final Pattern BAD_PREFIX = Pattern.compile("(?:\\}\\[)?(\\d\\d:\\d\\d:\\d\\d)\\] *");
  private static final Pattern SUBJECT_PTN = Pattern.compile("([-/ A-Z0-9]+)\\|(\\d\\d:\\d\\d:\\d\\d)");
  private static final Pattern MASTER = Pattern.compile("(?:([A-Z0-9]*),* )?at ([^,]*?)(?:, ([ A-Z]+))? - ([^,]*), (.*?)\\. ?\\$([A-Z]{3}\\d{8})\\. (\\d{8})\\.(?: ?([-+]?\\d{2}\\.\\d{6,})\\. ?([-+]?\\d{2}\\.\\d{6,})\\. ?\\. ?([-/A-Z0-9]*)\\.)?[\\. ]*", Pattern.DOTALL);

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      data.strCall = match.group(1).trim();
      data.strTime = match.group(2);
    } else {
      match = BAD_PREFIX.matcher(body);
      if (!match.lookingAt()) return false;
      data.strCall = subject;
      data.strTime =  match.group(1);
      body = body.substring(match.end());

    }

    match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = getOptGroup(match.group(1));
    parseAddress(match.group(2).trim(), data);
    data.strCity = convertCodes(getOptGroup(match.group(3)), CITY_CODES);
    data.strPlace = match.group(4).trim();
    data.strSupp = match.group(5).trim();
    data.strCallId = match.group(6) + '/' + match.group(7);
    setGPSLoc(getOptGroup(match.group(8)) + ',' + getOptGroup(match.group(9)), data);
    data.strMap = getOptGroup(match.group(10));
    return true;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AH",   "ARLINGTON HEIGHTS",
      "AL",   "ALGONQUIN",
      "BA",   "BARRINGTON",
      "BG",   "BUFFALO GROVE",
      "BH",   "BARRINGTON HILLS",
      "CL",   "CRYSTAL LAKE",
      "FR",   "FOX RIVER",
      "IN",   "INVERNESS",
      "LG",   "LONG GROVE",
      "LV",   "LIBERTYVILLE",
      "LZ",   "LAKE ZURICH",
      "LKBA", "LAKE BARRINGTON",
      "SB",   "SOUTH BARRINGTON",
      "WH",   "WHEELING"
  });
}
