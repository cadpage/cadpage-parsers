package net.anei.cadpage.parsers.FL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class FLCharlotteCountyBParser extends MsgParser {
  
  public FLCharlotteCountyBParser() {
    super("CHARLOTTE COUNTY", "FL");
    setFieldList("CODE CALL APT PLACE ADDR CITY ID PRI TIME GPS INFO");
  }
  
  @Override
  public String getFilter() {
    return "paging@ccso.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static Pattern CODE_CALL_PTN = Pattern.compile("(\\d{3}[A-Z]) +(.*)");
  private static Pattern APT_PTN = Pattern.compile("Unit/Apt *(.*)|Bld:.*");
  private static Pattern ID_PTN = Pattern.compile("\\d{4}-\\d{6}");
  private static Pattern GPS_PTN = Pattern.compile("(\\d{2})(\\d{6}) +(\\d{2})(\\d{6})");
  
  private static DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Paging")) return false;
    FParser fp = new FParser(body);
   
    String call = fp.get(30);
    Matcher match = CODE_CALL_PTN.matcher(call);
    if (match.matches()) {
      data.strCode = match.group(1);
      call = match.group(2);
    }
    data.strCall = call;
    
    String place = fp.get(50);
    match = APT_PTN.matcher(place);
    if (match.matches()) {
      data.strApt = match.group(1);
      if (data.strApt == null) data.strApt = place;
    } else {
      data.strPlace = place;
    }
    
    if (fp.check(" ")) return false;
    parseAddress(fp.get(50), data);
    
    data.strCity = fp.get(34);
    
    if (!fp.check(" ") || fp.check(" ")) return false;
    data.strCallId = fp.get(11);
    if (!ID_PTN.matcher(data.strCallId).matches()) return false;
    
    if (!fp.check("         ")) return false;
    data.strPriority = fp.get(1);
    
    String time = fp.get(11);
    if (!setTime(TIME_FMT, time, data)) return false;

    String gps = fp.get(18);
    if (gps.length() > 0) {
      match = GPS_PTN.matcher(gps);
      if (!match.matches()) return false;
      setGPSLoc(match.group(1)+'.'+match.group(2)+','+match.group(3)+'.'+match.group(4), data);
    }
    
    fp.skip(12);
    data.strSupp = fp.get();
    return true;
  }
}
