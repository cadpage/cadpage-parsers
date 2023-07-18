package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;

public class MIKalamazooCountyCParser extends MsgParser {

  public MIKalamazooCountyCParser() {
    super("KALAMAZOO COUNTY", "MI");
    setFieldList("CALL ADDR APT CITY PLACE INFO MAP X");
  }

  @Override
  public String getFilter() {
    return "paging@kccda911.org";
  }

  private static final Pattern MASTER = Pattern.compile("([^@]+)@([^/,]+?)(?:,([^/,]*))?(?:[/,](.*))?");
  private static final Pattern BRK_PTN = Pattern.compile("\\s*\n\\s*");
  private static final Pattern MAP_PTN = Pattern.compile("(?:[A-Z][a-z]+(?:-[A-Z][a-z]+)?(?: +(?:Twp|Village|Zone))?|SKCFA) +F?\\d+|Comstock +\\d-\\d-\\d");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("!DISP")) return false;

    body = stripFieldEnd(body, "//");

    String extra = null;
    int pt = body.indexOf('\n');
    if (pt >= 0) {
      extra = stripFieldStart(body.substring(pt+1).trim(), "//");
      body = body.substring(0, pt).trim();
    }

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    String addr = match.group(2).trim();
    parseAddress(addr.replace('@', '&'), data);
    String city = getOptGroup(match.group(3));
    String place = getOptGroup(match.group(4));

    pt = city.indexOf("  ");
    if (pt >= 0) {
      place = append(city.substring(pt+2).trim(), " - ", place);
      city = city.substring(0,pt);
    }
    data.strCity = city;
    data.strPlace = place;

    if (extra != null) {
      String[] lines = BRK_PTN.split(extra);
      int lastNdx = lines.length-1;
      String lastLine = lines[lastNdx];
      if (lastLine.equals("No Cross Streets Found")) {
        if (lastNdx == 0) return true;
        lastLine = lines[--lastNdx];
      } else if (lastLine.contains(" / ")) {
        data.strCross = lastLine;
        if (lastNdx == 0) return true;
        lastLine = lines[--lastNdx];
      }

      if (MAP_PTN.matcher(lastLine).matches()) {
        data.strMap = lastLine;
        lastNdx--;
      }

      for (int ndx = 0; ndx <= lastNdx; ndx++) {
        data.strSupp = append(data.strSupp, "\n", lines[ndx]);
      }
    }
    return true;
  }
}
