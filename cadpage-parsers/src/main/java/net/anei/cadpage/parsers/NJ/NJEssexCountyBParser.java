package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class NJEssexCountyBParser extends MsgParser {

  public NJEssexCountyBParser() {
    super("ESSEX COUNTY", "NJ");
    setFieldList("ID CALL PLACE ADDR APT CITY ST UNIT INFO");
  }

  @Override
  public String getFilter() {
    return "roselandpaging@enfwebmail.onmicrosoft.com";
  }

  private static final Pattern ID_PTN = Pattern.compile("\\d{4}-\\d{5}");
  private static final Pattern MASTER = Pattern.compile("(.*?) @ (?:(.*) - )?(.*?), ([ A-Z]+) ([A-Z]{2}) \\d{5} \nActive Units: *(\\S+) - *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!ID_PTN.matcher(subject).matches()) return false;
    data.strCallId = subject;

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    data.strPlace = getOptGroup(match.group(2));
    parseAddress(match.group(3).trim(), data);
    data.strCity = match.group(4).trim();
    data.strState = match.group(5);
    data.strUnit = match.group(6);
    data.strSupp = match.group(7);
    return true;

  }

}
