package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class NYSuffolkCountyLParser extends MsgParser {

  public NYSuffolkCountyLParser() {
    super("SUFFOLK COUNTY", "NY");
    setFieldList("TIME CALL PLACE ADDR APT CITY ST INFO");
  }

  @Override
  public String getFilter() {
    return "alertpage@alertpage.net";
  }

  private static final Pattern MASTER = Pattern.compile("(\\d\\d:\\d\\d): ([^;]*?); ([^()]*?)\\((.*)\\)");
  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}");
  private static final Pattern CITY_PTN = Pattern.compile("[- A-Z]+");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Dispatches from ")) return false;

    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strTime = match.group(1);
    data.strCall = match.group(2).trim();
    if (data.strCall.length() == 0) data.strCall = "ALERT";
    String addr = match.group(3).trim();
    data.strSupp = match.group(4);

    Parser p = new Parser(addr);
    String part = p.getLast(',');
    if (STATE_PTN.matcher(part).matches()) {
      data.strState = part;
      part = p.getLast(',');
    }
    if (CITY_PTN.matcher(part).matches()) {
      data.strCity = part;
      part = p.getLast(',');
    }
    parseAddress(part, data);
    data.strPlace = p.get();
    return true;
  }

}
