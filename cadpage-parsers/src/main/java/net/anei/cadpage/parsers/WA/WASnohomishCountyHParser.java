package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class WASnohomishCountyHParser extends SmartAddressParser {

  public WASnohomishCountyHParser() {
    super(WASnohomishCountyParser.CITY_LIST, "SNOHOMISH COUNTY", "WA");
    setFieldList("UNIT CALL ADDR APT CITY ST MAP CH");
    setupCities(BAD_CITIES);
  }

  @Override
  public String getFilter() {
    return "@sno911.org";
  }

  private static final Pattern MASTER = Pattern.compile("(.*?) (MED\\d|BLS\\d)(?: RESPONSE)?[, ]+(.*?)[, ]+(?:GRID )?([A-Z]{2}\\d{4})[, ]+(?:RESPOND ON +)?(TAC ?\\d{1,2})\\.?");
  private static final Pattern TRAIL_ST_PTN = Pattern.compile("(.*), *([A-Z]{2})");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("SNO911 ALERT")) return false;
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = stripFieldEnd(match.group(1).replace(" ", ""), ",");
    data.strCall = match.group(2);
    String addr = match.group(3).trim();
    data.strMap =  match.group(4);
    data.strChannel = match.group(5).replace(" ", "");

    match = TRAIL_ST_PTN.matcher(addr);
    if (match.matches()) {
      addr = match.group(1).trim();
      data.strState = match.group(2);
    }
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
    data.strCity = convertCodes(data.strCity, BAD_CITIES);
    return true;
  }

  private static final Properties BAD_CITIES = buildCodeTable(new String[] {
      "MNOROE",   "MUNROE"
  });
}
