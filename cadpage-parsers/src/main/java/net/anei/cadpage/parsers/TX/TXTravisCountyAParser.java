package net.anei.cadpage.parsers.TX;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;
/**
 * Travis County, TX
 */
public class TXTravisCountyAParser extends MsgParser {

  public TXTravisCountyAParser() {
    super("TRAVIS COUNTY", "TX");
  }
  
  public String getFilter() {
    return "PublicSafety@austintexas.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern COMMENT_PTN = Pattern.compile("Comment: (.*?), (From - .*?)(?: From - .*)?");
  
  private static final Pattern MASTER1 = 
      Pattern.compile("From -([A-Z0-9]+) Dispatch - ?\\d?ALARM -(.*?) - BOX -([-A-Z0-9]*) On -([ A-Z0-9]*) - AT -(.+?) - INC# =>(\\d+) Case Num:([- A-Z0-9]*) For -([A-Z0-9,]+)");
  
  private static final Pattern MASTER2 = 
      Pattern.compile("From - ?([A-Z0-9]+) - ?\\d ?Alarm / ?(.*?) (?:Pri (\\d+) +)?(?:Box|BOX|\\| RAP) - ?([-A-Z0-9]*) ?@ ?(.*?) (?:\\| )?XStreets: *(.*?)[ \\|]+?On - ?([ A-Z0-9]*)\\|? Time:[ \\|]*(?:(\\d\\d:\\d\\d:\\d\\d)|(\\d\\d:\\d\\d [AP]M))[ \\|]+Inc# ?(\\d+)(?: Case Num:([-A-Z0-9]*))?[ \\|]+For - ?([A-Z0-9,]+)");
  
  private static final Pattern MASTER3 = 
      Pattern.compile("(?:INCIDENT ASSIGNED TO YOU!!*|Response Info from Dispatch) - ?\\d?ALARM -(.*?) - BOX -([-A-Z0-9]*) FC=(.+?)- AT -(.*?) - ASSIGNED UNITS=>([A-Z0-9,]+)- INC# =>(\\d+)");
  
  private static final Pattern CROSS_PTN = Pattern.compile("(?:(?:N?o )?CrossStreet Found)?/?(.*?)/?(?:No CrossStreet Found)?");
  
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
 
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = COMMENT_PTN.matcher(body);
    if (match.matches()) {
      data.strSupp = match.group(1).trim();
      body = match.group(2).trim();
    }
    match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("INFO SRC CALL MAP CH ADDR CITY APT ID UNIT");
      
      data.strSource = match.group(1);
      data.strCall = match.group(2).trim();
      data.strMap = match.group(3).trim();
      data.strChannel = match.group(4).trim();
      parseAddress(match.group(5).trim(), data);
      data.strCallId = append(match.group(7), "/", match.group(6));
      data.strUnit = match.group(8);
      
      return true;
    }
    
    match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("INFO SRC CALL PRI MAP ADDR CITY APT X CH TIME ID UNIT");
      
      data.strSource = match.group(1);
      data.strCall = match.group(2).trim();
      data.strPriority = getOptGroup(match.group(3));
      data.strMap = match.group(4).trim();
      parseAddress(match.group(5).trim(), data);
      String cross  = match.group(6).trim();
      data.strChannel = match.group(7);
      String time = match.group(8);
      if (time != null) data.strTime = time;
      else setTime(TIME_FMT, match.group(9), data);
      data.strCallId = append(match.group(10), "/", getOptGroup(match.group(11)));
      data.strUnit = match.group(12);
      match = CROSS_PTN.matcher(cross);
      if (match.matches()) cross = match.group(1);
      data.strCross = cross;
      
      return true;
      
    }
    
    match = MASTER3.matcher(body);
    if (match.matches()) {
      setFieldList("CALL MAP CH ADDR CITY APT UNIT ID");
      data.strCall = match.group(1);
      data.strMap = match.group(2);
      data.strChannel = match.group(3).trim();
      parseAddress(match.group(4).trim(), data);
      data.strUnit = match.group(5).trim();
      data.strCallId = match.group(6);
      return true;
    }
    
    return false;
  }
  
  @Override
  public void parseAddress(String addr, Data data) {
    int pt = addr.lastIndexOf(',');
    if (pt >= 0) {
      data.strCity = convertCodes(addr.substring(pt+1).trim(), CITY_CODES);
      addr = addr.substring(0,pt).trim();
    }
    super.parseAddress(addr, data);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BAS", "BASTROP COUNTY",
      "BLC", "BLANCO COUNTY",
      "BUC", "BURNET COUNTY",
      "CAC", "CALDWELL COUNTY",
      "HAC", "HAYS COUNTY",
      "TC",  "TRAVIS COUNTY",
      "WSC", "WILLIAMSON COUNTY"
  });
}
