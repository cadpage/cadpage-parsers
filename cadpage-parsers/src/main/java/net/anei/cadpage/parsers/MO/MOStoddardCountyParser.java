package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class MOStoddardCountyParser extends MsgParser {

  public MOStoddardCountyParser() {
    super("STODDARD COUNTY", "MO");
    setFieldList("ADDR APT CITY ST INFO ID NAME PLACE UNIT MAP CALL DATE TIME");
  }

  @Override
  public String getFilter() {
    return "noreply@stoddems.com";
  }

  private static final Pattern MASTER =
      Pattern.compile("(?:([^;,]*(?:, -\\d+\\.\\d+ *)?(?:, APT [A-Z0-9]+ *)?)(?:, ([ A-Z]+))?(?:, ([A-Z]{2})(?: \\d{5})?)? )?([^;]*) ((?:[A-Z0-9]+; )*)((?!911)\\d{3}|[A-Z]FD) (?:(.*) )?Call Time: \\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d Assigned (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d)\\b.*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(getOptGroup(getOptGroup(match.group(1))), data);
    data.strCity = getOptGroup(match.group(2));
    data.strState = getOptGroup(match.group(3));
    String info = match.group(4).trim();
    data.strUnit = match.group(5).replace(";", "").trim();
    data.strMap = match.group(6);
    data.strCall = getOptGroup(match.group(7));
    data.strDate = match.group(8);
    data.strTime = match.group(9);
    if (subject.length() > data.strCall.length()) data.strCall = subject;

    if (!info.isEmpty() && !info.equals("None")) {
      if (info.endsWith(" --")) info += ' ';
      String[] infoFlds = info.split(" -- ", -1);
      if (infoFlds.length == 1) {
         parseInfoFld(info.trim(), data);
      } else {
        data.strSupp = infoFlds[0].trim();
        for (int ndx = 1; ndx < infoFlds.length; ndx++) {
          parseInfoFld(infoFlds[ndx].trim(), data);
        }
      }
    }
    return true;
  }

  private void parseInfoFld(String info, Data data) {
    if (info.isEmpty()) return;
    if (info.startsWith("#")) {
      data.strCallId = info.substring(1).trim();
    } else if (info.contains("/")) {
      data.strName = info;
    } else {
      data.strPlace = info;
    }

  }
}
