package net.anei.cadpage.parsers.ZSE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZSESwedenCParser extends ZSESwedenBaseParser {

  public ZSESwedenCParser() {
    super("", "",
          "CALL CALL CALL CALL INFO ADDR CITY UNIT CH GPS! END");
  }

  @Override
  public String getLocName() {
    return "Contal";
  }

  @Override
  public String getFilter() {
    return "46729778078";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MASTER = Pattern.compile("(.*?) (RAPS-\\d+) (.*?) ([DT]\\d{1,2}) (.*?) (La = .*?  Lo = .*)");
  private static final Pattern ADDR_PTN = Pattern.compile("\\b(\\p{IsAlphabetic}+ \\d+|\\p{IsAlphabetic}+ X \\p{IsAlphabetic}+)\\b");

  protected boolean parseMsg(String body, Data data) {
    String[] flds = body.split("\n");
    if (flds.length > 1) return parseFields(body.split("\n"), data);

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    setFieldList("CALL CH ADDR CITY UNIT GPS");
    data.strCall = match.group(1).trim();
    data.strChannel = match.group(2);
    String callAddr = match.group(3).trim();
    data.strUnit = match.group(4);
    String city2 = match.group(5);
    parseGPSField(match.group(6), data);

    // Parsing Swedish addresses is a challenge :(
    match = ADDR_PTN.matcher(callAddr);
    if (match.find()) {
      data.strAddress = match.group().replace(" X ", " & ");
      data.strCity = callAddr.substring(match.end()).trim();
      callAddr = callAddr.substring(0,match.start()).trim();
    }
    data.strCall = append(data.strCall, " - ", callAddr);
    if (data.strCity.isEmpty()) data.strCity = city2;
    return true;
  }
}
