package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class ALShelbyCountyCParser extends MsgParser {

  public ALShelbyCountyCParser() {
    super("SHELBY COUNTY", "AL");
    setFieldList("CALL ADDR APT CITY ST INFO");
  }

  @Override
  public String getFilter() {
    return "bappleby@shelby911.org";
  }

  private static final Pattern MASTER = Pattern.compile("([- A-Z0-9]+) at  ([^,]*?), ([ A-Z]+), ([A-Z]{2})(?: \\d{5})? Notes: *(.*?)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldEnd(body, " Are you responding? (Y or N)");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    parseAddress(match.group(2).trim(), data);
    data.strCity = match.group(3).trim();
    data.strState = match.group(4);
    data.strSupp = match.group(5).trim();
    return true;
  }

}
