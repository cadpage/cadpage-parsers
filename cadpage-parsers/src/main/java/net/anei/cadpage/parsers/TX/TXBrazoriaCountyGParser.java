package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class TXBrazoriaCountyGParser extends MsgParser {

  public TXBrazoriaCountyGParser() {
    super("BRAZORIA COUNTY", "TX");
    setFieldList("CALL ID ADDR APT CITY ST DATE TIME UNIT INFO");
  }

  @Override
  public String getFilter() {
    return "donotreply@angletonpd.net";
  }

  private static final Pattern MASTER = Pattern.compile("(\\d{4}-\\d{6}) ([^,]+?), *([A-Z ]+), *([A-Z]{2}) +\\d{5} (\\d\\d?/\\d\\d?/\\d\\d) (\\d\\d?:\\d\\d) ([A-Z][A-Z0-9]*)(?: +\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d - +(.*))?");
  private static final Pattern INFO_PTN = Pattern.compile("[; ]+\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d - +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty()) return false;
    data.strCall = subject;

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    data.strCallId = match.group(1);
    parseAddress(match.group(2).trim(), data);
    data.strCity = match.group(3).trim();
    data.strState = match.group(4);
    data.strDate = match.group(5);
    data.strTime = match.group(6);
    data.strUnit = match.group(7);
    String info = match.group(8).trim();
    if (info != null) {
      data.strSupp = INFO_PTN.matcher(info).replaceAll("\n");
    }
    return true;
  }

}
