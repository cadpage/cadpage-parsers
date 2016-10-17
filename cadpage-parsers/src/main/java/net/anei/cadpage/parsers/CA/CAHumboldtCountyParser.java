package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

/**
 * Humbolt County, CA
 */
public class CAHumboldtCountyParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("RA: +([A-Z0-9]+); +([^;]+); +([^,]+?) +,([^ ]*?) +X: +(-?[ \\d\\.]+?) +Y: +(-?[ \\d\\.]+?) +Inc# +(\\d+); +([^;]+?) *; *(?:Descr: *)?(.*)");
  
  public CAHumboldtCountyParser() {
    super("HUMBOLDT COUNTY", "CA");
    setFieldList("SRC CALL ADDR APT CITY GPS ID UNIT INFO");
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
    data.strCity = match.group(fld++).replace('_', ' ');
    setGPSLoc(match.group(fld++)+','+match.group(fld++), data);
    data.strCallId = match.group(fld++);
    data.strUnit = match.group(fld++);
    String info = match.group(fld++);
    int pt = info.indexOf(" <a href");
    if (pt >= 0) info = info.substring(0,pt).trim();
    data.strSupp = info;
    return true;
  }
}
