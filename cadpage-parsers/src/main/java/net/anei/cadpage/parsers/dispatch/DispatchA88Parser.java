package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class DispatchA88Parser extends SmartAddressParser {

  public DispatchA88Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState);
    setFieldList("ID PLACE ADDR APT CITY CALL");
  }

  private static final Pattern MASTER = Pattern.compile("(?:\\d+-)?CFS Report (\\d{4}-\\d{6}) (?:([^|,]*)\\|)?([^,]*),(.*)");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = append(subject, " | ", body);
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strPlace = getOptGroup(match.group(2));
    parseAddress(match.group(3).trim(), data);
    parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, match.group(4).trim(), data);
    data.strCall = getLeft();
    return !data.strCity.isEmpty();
  }
}
