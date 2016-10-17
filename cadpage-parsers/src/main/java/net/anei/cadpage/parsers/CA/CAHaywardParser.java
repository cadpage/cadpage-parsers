package net.anei.cadpage.parsers.CA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class CAHaywardParser extends MsgParser {

  public CAHaywardParser() {
    super("HAYWARD", "CA");
    setFieldList("ADDR APT GPS PLACE CALL DATE TIME MAP UNIT INFO");
  }

  private static Pattern MASTER = 
      Pattern.compile("(?:(.*?) )?(?:(\\d+\\.\\d{5,} -\\d+\\.\\d{5,})|-361 -361) (?:(.*?) )?([A-Z]{2,7}) (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)([A-Za-z]{3} [A-Za-z0-9]{4,5} \\d{1,4})(?:  +(.*))?");
  private static DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private static Pattern UNIT_PTN = Pattern.compile("\\bDispatch received by unit ([A-Z0-9]+) *");
  private static Pattern UNIT_PTN2 = Pattern.compile("(?: *\\b[A-Z]+\\d+)+$");

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Incident Notification")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(getOptGroup(match.group(1)), data);
    String gps = match.group(2);
    if (gps != null) {
      if (data.strAddress.length() == 0) data.strAddress = gps;
      else setGPSLoc(gps, data);
    }
    if (data.strAddress.length() == 0) return false;
    
    data.strPlace = getOptGroup(match.group(3));
    data.strCall = match.group(4);
    data.strDate = match.group(5);
    setTime(TIME_FMT, match.group(6), data);
    data.strMap = match.group(7);
    String info = getOptGroup(match.group(8));
    
    match = UNIT_PTN.matcher(info);
    int pt = 0;
    while (match.find()) {
      data.strSupp = append(data.strSupp, "\n", info.substring(pt,match.start()).trim());
      data.strUnit = append(data.strUnit, " ", match.group(1));
      pt = match.end();
    }
    
    info = info.substring(pt);
    match = UNIT_PTN2.matcher(info);
    if (match.find()) {
      data.strUnit = append(data.strUnit, " ", match.group().trim());
      info = info.substring(0,match.start());
    }
    data.strSupp = append(data.strSupp, "\n", info);
    return true;
  }
}
