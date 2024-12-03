
package net.anei.cadpage.parsers.AZ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class AZYavapaiCountyBParser extends MsgParser {

  public AZYavapaiCountyBParser() {
    super("YAVAPAI COUNTY", "AZ");
    setFieldList("CALL MAP DATE TIME ADDR APT SRC X UNIT INFO GPS");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern MASTER =
      Pattern.compile("(.*?) Map(\\d+) +(\\d\\d?/\\d\\d/\\d\\d) +(\\d\\d:\\d\\d:\\d\\d) *ADD:(.*?) *([A-Z]+) *XST:(.*?) *UNITS:(\\S*) (?:CMT: *)?(.*) LAT: *(\\d+) +LON: *(\\d+)");

  @Override
  public boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "PN:");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    data.strMap = match.group(2);
    data.strDate = match.group(3);
    data.strTime = match.group(4);
    parseAddress(match.group(5).trim(), data);
    data.strSource = match.group(6);
    data.strCross = stripFieldEnd(match.group(7), "/");
    data.strUnit = match.group(8);
    data.strSupp = match.group(9).trim();
    setGPSLoc(setDec(match.group(10))+','+setDec(match.group(11)), data);
    return true;
  }

  private static String setDec(String fld) {
    int pt = fld.length()-6;
    if (pt >= 0) fld = fld.substring(0,pt)+'.'+fld.substring(pt);
    return fld;
  }
}
