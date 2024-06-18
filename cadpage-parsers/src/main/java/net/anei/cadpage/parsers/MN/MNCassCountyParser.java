package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class MNCassCountyParser extends MsgParser {

  public MNCassCountyParser() {
    this("CASS COUNTY", "MN");
  }

  MNCassCountyParser(String defCity, String defState) {
    super(defCity, defState);
    setFieldList("CALL ADDR APT MAP CITY ST DATE TIME INFO");
  }

  @Override
  public String getFilter() {
    return "no-reply@ledsportal.com,zuercher.cass@co.cass.mn.us,zuercher.cass@casscountymn.gov";
  }

  private static final Pattern MASTER = Pattern.compile("(.*?) RESPOND(?: (?:None|(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) - *(.*)))?");
  private static final Pattern ADDR_PTN = Pattern.compile("(.*?)(?:\\((.*?)\\))?(?:, *([ A-Z]{3,}))?(?:, *([A-Z]{2}))?(?: (\\d{5}))?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    data.strCall = subject;

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    String addr = match.group(1).trim();
    data.strDate = getOptGroup(match.group(2));
    data.strTime = getOptGroup(match.group(3));
    data.strSupp = getOptGroup(match.group(4));

    match = ADDR_PTN.matcher(addr);
    if (match.matches()) {
      addr = match.group(1).trim();
      data.strMap = getOptGroup(match.group(2));
      data.strCity = getOptGroup(match.group(3));
      data.strState = getOptGroup(match.group(4));
      if (data.strCity.length() == 0) data.strCity = getOptGroup(match.group(5));
    }
    parseAddress(addr, data);

    return true;
  }

}
