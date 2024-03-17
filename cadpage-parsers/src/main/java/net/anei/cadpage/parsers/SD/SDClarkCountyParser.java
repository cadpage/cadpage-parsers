package net.anei.cadpage.parsers.SD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class SDClarkCountyParser extends MsgParser {

  public SDClarkCountyParser() {
    super("CLARK COUNTY", "SD");
    setFieldList("CALL ADDR APT CITY ST INFO");
  }

  private static final Pattern MASTER = Pattern.compile("([-/ A-Za-z0-9]+?), ([A-Z ]+), ([A-Z]{2}) \\d{5} (.*)");
  @Override
  public String getFilter() {
    return "no-reply@watertownsd.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strCall = subject;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(match.group(1).trim(), data);
    data.strCity = match.group(2).trim();
    data.strState = match.group(3);
    String info = match.group(4).trim();
    if (data.strCall.isEmpty()) {
      data.strCall = info;
    } else {
      data.strSupp = info;
    }
    return true;
  }

}
