package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;

public class ILWinnebagoCountyBParser extends SmartAddressParser {

  public ILWinnebagoCountyBParser() {
    super(CITY_LIST, "WINNEBAGO COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "DoNotReply@rockfordil.gov";
  }

  private static final Pattern MASTER1 = Pattern.compile("(.+?) (HR\\d\\d-\\d{6}) (.+?)(?: ([A-Z][A-Z0-9]*\\dS?))?");
  private static final Pattern MASTER2 = Pattern.compile("(.+?) (HR\\d\\d-\\d{5,6})(\\d\\d/\\d\\d/\\d{4})([A-Z][A-Z0-9]*\\dS?|) (\\[1\\].*)");
  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *, *(?=\\[\\d{1,2}\\])");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Alert")) return false;
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ID ADDR APT CITY UNIT");
      data.strCall = match.group(1).trim();
      data.strCallId = match.group(2);
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(3).trim(), data);
      if (data.strCity.isEmpty()) return false;
      data.strUnit = getOptGroup(match.group(4));
      return true;
    }

    match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ADDR APT CITY ID DATE UNIT INFO");
      parseAddress(StartType.START_CALL, FLAG_ANCHOR_END, match.group(1).trim(), data);
      if (data.strCity.isEmpty()) return false;
      data.strCallId = match.group(2);
      data.strDate =  match.group(3);
      data.strUnit = match.group(4);
      String info = match.group(5);
      for (String line : INFO_BRK_PTN.split(info)) {
        line = stripFieldEnd(line, ",");
        line = stripFieldEnd(line, "[Shared]");
        data.strSupp = append(data.strSupp, "\n", line);
      }
      return true;
    }

    return false;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "LOVES PARK",
      "ROCKFORD",
      "SOUTH BELOIT",

      // Villages
      "CHERRY VALLEY",
      "DURAND",
      "MACHESNEY PARK",
      "NEW MILFORD",
      "PECATONICA",
      "ROCKTON",
      "ROSCOE",
      "WINNEBAGO",

      // Census-designated place
      "LAKE SUMMERSET",

      // Unincorporated communities
      "ALWORTH",
      "ARGYLE",
      "HARLEM",
      "HARRISON",
      "KISHWAUKEE",
      "LATHAM PARK",
      "SEWARD",
      "SHIRLAND",
      "WEMPLETOWN",
      "WESTFIELD CORNERS",

      // Townships
      "BURRITT",
      "CHERRY VALLEY",
      "DURAND",
      "HARLEM",
      "HARRISON",
      "LAONA",
      "OWEN",
      "PECATONICA",
      "ROCKFORD",
      "ROCKTON",
      "ROSCOE",
      "SEWARD",
      "SHIRLAND",
      "WINNEBAGO",

      // Former Settlement
      "CAMP GRANT",

      // Counties
      "BOONE COUNTY",
      "WINNEBAGO COUNTY"
  };

}
