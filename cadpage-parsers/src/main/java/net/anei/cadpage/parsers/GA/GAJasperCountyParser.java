package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class GAJasperCountyParser extends MsgParser {

  public GAJasperCountyParser() {
    super("JASPER COUNTY", "GA");
    setFieldList("CALL ID ADDR APT CITY ST INFO");
  }

  @Override
  public String getFilter() {
    return "911paging@jasperema911ga.com";
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private static final Pattern ID_ADDR_PTN = Pattern.compile("(CFS\\d{4}-\\d{8}) +([^,]*?)(?:, *([-A-Z ]+)(?:, *([A-Z]{2})(?: +\\d{5})?)?)?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty()) return false;
    if (!body.startsWith(subject+' ')) return false;
    data.strCall = subject;
    body = body.substring(subject.length()+1).trim();
    body = stripFieldEnd(body, " None");
    body = stripFieldEnd(body, " 0.0");

    String[] parts = INFO_BRK_PTN.split(body);
    if (parts.length < 2) return false;

    Matcher match = ID_ADDR_PTN.matcher(parts[0]);
    if (!match.matches()) return false;

    data.strCallId = match.group(1);
    parseAddress(match.group(2).trim(), data);
    data.strCity = getOptGroup(match.group(3));
    data.strState = getOptGroup(match.group(4));

    for (int ii = 1; ii<parts.length; ii++) {
      data.strSupp = append(data.strSupp, "\n", parts[ii]);
    }
    return true;
  }
}
