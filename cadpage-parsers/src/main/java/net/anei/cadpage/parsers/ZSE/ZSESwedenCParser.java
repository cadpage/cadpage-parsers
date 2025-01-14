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
    return "46729778,46706031127";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MASTER = Pattern.compile("(.*?) (RAPS-\\d+|\\d+SjvIns-\\d) (.*?) (?:([DT]\\d{1,2}[A-Z]?) (.*?) )?(La = .*?  Lo = .*)");
  private static final Pattern ADDR_PTN;
  static {

    // It seems that very old versions of Android do not support unicode expressions
    Pattern ptn;
    try {
      ptn = Pattern.compile("\\b(\\p{IsAlphabetic}+ \\d+[A-Z]?|\\p{IsAlphabetic}+ X \\p{IsAlphabetic}+)\\b", Pattern.UNICODE_CHARACTER_CLASS);
    } catch (Exception ex) {
      ptn = Pattern.compile("\\b(\\w+ \\d+[A-Z]?|\\w+ X \\w+)\\b", Pattern.UNICODE_CHARACTER_CLASS);
    }
    ADDR_PTN = ptn;
  }

  protected boolean parseMsg(String body, Data data) {
    String[] flds = body.split("\n");
    if (flds.length > 1) return parseFields(body.split("\n"), data);

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    setFieldList("CALL CH ADDR CITY UNIT GPS");
    data.strCall = match.group(1).trim();
    data.strChannel = match.group(2);
    String callAddr = match.group(3).trim();
    String unit = match.group(4);
    String city2 = match.group(5);
    parseGPSField(match.group(6), data);

    // Parsing Swedish addresses is a challenge :(
    match = ADDR_PTN.matcher(callAddr);
    if (match.find()) {
      data.strAddress = match.group().replace(" X ", " & ").replace(" x ", " & ");
      data.strCity = callAddr.substring(match.end()).trim();
      data.strCall = append(data.strCall, " - ", callAddr.substring(0,match.start()).trim());
    } else {
      data.strAddress = callAddr;
    }

    if (unit == null) {
      if (data.strCity.isEmpty()) return false;
      Parser p = new Parser(data.strCity);
      data.strCity = p.get(' ');
      unit = p.get(' ');
      city2 = p.get();
    }

    data.strUnit = unit;

    if (data.strCity.isEmpty()) data.strCity = city2;
    return true;
  }
}
