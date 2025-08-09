package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

/**
 * Humbolt County, CA
 */
public class CAHumboldtCountyAParser extends MsgParser {

  private static final Pattern MASTER = Pattern.compile("RA: +([A-Z0-9]+); +([^;]+); +([^;]+?) +,([A-Z][^ ]*?) +(?:; *(.*?))? +X: +(-?[ \\d\\.]+?) +Y: +(-?[ \\d\\.]+?) +Inc# +(\\d+); +([^;]+?) *; *(?:Descr: *)?(.*)", Pattern.DOTALL);

  public CAHumboldtCountyAParser() {
    super("HUMBOLDT COUNTY", "CA");
    setFieldList("SRC CALL ADDR APT CITY GPS ID UNIT INFO CH MAP");
  }

  @Override
  public String getFilter() {
    return "huucad@fire.ca.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    int fld = 1;
    data.strSource = match.group(fld++);
    data.strCall = match.group(fld++);
    parseAddress(match.group(fld++), data);
    data.strCity = stripFieldEnd(match.group(fld++).replace('_', ' '), " STN");
    data.strCall = append(data.strCall, " - ", getOptGroup(match.group(fld++)));
    setGPSLoc(match.group(fld++)+','+match.group(fld++), data);
    data.strCallId = match.group(fld++);
    data.strUnit = match.group(fld++);
    String info = match.group(fld++);
    int pt = info.indexOf(" <a href");
    if (pt >= 0) info = info.substring(0,pt).trim();
    for (String part : info.split(";")) {
      part = part.trim();

      pt = part.indexOf("Map:");
      if (pt >= 0) {
        data.strMap = append(data.strMap, "/", part.substring(pt+4).trim());
        part = part.substring(0,pt).trim();
      }

      String channel = "";
      pt = part.indexOf("Tac:");
      if (pt >= 0) {
        channel = part.substring(pt+4).trim();
        part = part.substring(0,pt).trim();
      }

      pt = part.indexOf("Cmd:");
      if (pt >= 0) {
        channel = append(part.substring(pt+4).trim(), "/", channel);
        part = part.substring(0,pt).trim();
      }

      data.strChannel = append(data.strChannel, "/", channel);

      if ("Cmd:".startsWith(part) || "Tac:".startsWith(part) || "Map:".startsWith(part)) continue;

      data.strSupp = append(data.strSupp, "\n", part);
    }
    return true;
  }
}
