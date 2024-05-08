package net.anei.cadpage.parsers.SD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class SDHydeCountyParser extends MsgParser {

  public SDHydeCountyParser() {
    super("HYDE COUNTY", "SD");
    setFieldList("ADDR APT CITY ST CALL PLACE INFO");
  }

  @Override
  public String getFilter() {
    return "Zuercher@co.hughes.sd.us";
  }

  private static final Pattern MASTER = Pattern.compile("Location: (.*?)(?:, *([A-Z ]+))?(?:, *([A-Z]{2})(?: (\\d{5}))?)? /+(?:([^/]*) CFS Type: (.*) )?/+ ([^/]*)\\. \\. (.*)\\. \\. \\.(?: (.*))?");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(match.group(1).trim(), data);
    data.strCity = getOptGroup(match.group(2));
    data.strState = getOptGroup(match.group(3));
    if (data.strCity.isEmpty()) data.strCity =  getOptGroup(match.group(4));
    data.strSupp = getOptGroup(match.group(5));
    data.strCall = getOptGroup(match.group(6));
    data.strPlace = getOptGroup(match.group(7));
    data.strSupp = append(data.strSupp, "\n", getOptGroup(match.group(9)));

    if (data.strCall.isEmpty()) {
      data.strCall = subject;
    } else if (subject.equals("DISREGARD")) {
      data.strCall = subject + " - " + data.strCall;
    }

    return true;
  }

}
