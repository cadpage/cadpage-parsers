
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
      Pattern.compile("(.*?) (?:Map|MAP:)(\\d+) +(?:(\\d\\d?/\\d\\d/\\d\\d) +)?(\\d\\d:\\d\\d:\\d\\d) *ADD:(.*?) *([A-Z]+) *XST:(.*)");
  private static final Pattern GPS_PTN = Pattern.compile("(.*)\\bLAT: *(\\d+) +LON: *(\\d+)");
  private static final Pattern UNIT_PTN = Pattern.compile("(.*)\\bUNITS:(\\S*)\\b *(.*)");

  @Override
  public boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "PN:");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    data.strMap = match.group(2);
    data.strDate = getOptGroup(match.group(3));
    data.strTime = match.group(4);
    parseAddress(match.group(5).trim(), data);
    data.strSource = match.group(6);

    body = match.group(7).trim();
    match = GPS_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      setGPSLoc(setDec(match.group(2))+','+setDec(match.group(3)), data);
    }
    match = UNIT_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strUnit = match.group(2).trim();
      data.strSupp = stripFieldStart(match.group(3), "CMT:");
    } else {
      int pt = body.indexOf(" CMT:");
      if (pt >= 0) {
        data.strSupp = body.substring(pt+4).trim();
        body = body.substring(0,pt).trim();
      }
    }
    data.strCross = stripFieldEnd(body, "/");
    return true;
  }

  private static String setDec(String fld) {
    int pt = fld.length()-6;
    if (pt >= 0) fld = fld.substring(0,pt)+'.'+fld.substring(pt);
    return fld;
  }
}
