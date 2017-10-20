package net.anei.cadpage.parsers.WV;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class WVKanawhaCountyParser extends SmartAddressParser {
  
  public WVKanawhaCountyParser() {
    super("KANAWHA COUNTY", "WV");
    setFieldList("CALL ADDR APT CITY PLACE DATE TIME ID");
  }
  
  @Override
  public String getFilter() {
    return "@metro911.org";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("Message Forwarded by PageGate")) {
      return parseMsg1(body, data);
    } else {
      return parseMsg2(body, data);
    }
  }
  
  private static final Pattern AT_ON_PTN = Pattern.compile(" (?:at|on) ");
  
  private boolean parseMsg1(String body, Data data) {
    body = stripFieldEnd(body, ".");
    body = stripFieldStart(body, "Daytime Notifications: - ");
    Parser p = new Parser(body);
    data.strCall = stripFieldEnd(p.get(AT_ON_PTN), " reported");
    data.strCity = p.getLastOptional(" in ");
    String addr = p.get();
    addr = stripFieldStart(addr, "the ");
    if (addr.length() == 0) return false;
    parseAddress(addr, data);
    return true;
  }
  
  private static final Pattern MASTER2 = 
      Pattern.compile("(?:Metro911:|Metro CAD Alert:)?(.+?) reported at (.+?)(?:@([^,]*)?)?(?:(?:/? in |, +)(.+?))?(?:\\((.*?)\\))? on (\\d\\d?/\\d\\d?/\\d\\d(?:\\d\\d)?) (\\d\\d?:\\d\\d(?::\\d\\d [AP]M)?)(?: +Call (?:#|Number:) *(.*))?");
  private static DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  private boolean parseMsg2(String body, Data data) {
    body = body.replace('\n', ' ');
    Matcher match = MASTER2.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END | FLAG_IMPLIED_INTERSECT, match.group(2).trim().replaceAll("//", "/"), data);
    data.strPlace = getOptGroup(match.group(3));
    data.strCity = getOptGroup(match.group(4));
    if (data.strCity.equals("SISSONVILLE")) {
      if (data.strPlace.length() == 0) data.strPlace = data.strCity;
      data.strCity = "CHARLESTON";
    }
    data.strPlace = append(data.strPlace, " - ", getOptGroup(match.group(5)).replace(") (", " - "));
    data.strDate = match.group(6);
    String time  = match.group(7);
    if (time.endsWith("M")) {
      setTime(TIME_FMT, time, data);
    } else {
      data.strTime = time;
    }
    data.strCallId = getOptGroup(match.group(8));
    return true;
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("LITTLE TYLER")) return "3";
    return city;
  }
}
