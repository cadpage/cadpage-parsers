package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
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
      Pattern.compile("(?:([^;,]*(?:, -\\d+\\.\\d+ *)?(?:, APT [A-Z0-9]+ *)?)(?:, ([ A-Z]+))?(?:, ([A-Z]{2})(?: \\d{5})?)? )?([^;]*) ((?:[A-Z0-9]+; )*)((?!911)\\d{2,3}|SCSO|[A-Z]{1,2}[FP]D\\d?) (?:(.*) )?Call Time: \\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d Assigned (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d)\\b *(.*)");
  private static final Pattern TIMES_BRK_PTN = Pattern.compile("[ \\*]*; *");
  private static final Pattern TIME_PTN = Pattern.compile("(([A-Z][a-z]+) \\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d)\\b.*");

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
    String times = match.group(10);
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

    StringBuilder sb = new StringBuilder();
    times = stripFieldStart(times, ";");
    for (String time : TIMES_BRK_PTN.split(times)) {
      match = TIME_PTN.matcher(time);
      if (match.matches()) {
        if (sb.length() > 0) sb.append('\n');
        sb.append(match.group(1));
        if (match.group(2).equals("Completed")) {
          data.msgType = MsgType.RUN_REPORT;
          data.strSupp = append(sb.toString(), "\n", data.strSupp);
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
