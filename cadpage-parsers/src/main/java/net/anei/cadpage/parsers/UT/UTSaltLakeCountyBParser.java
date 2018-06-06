package net.anei.cadpage.parsers.UT;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class UTSaltLakeCountyBParser extends SmartAddressParser {
  
  public UTSaltLakeCountyBParser() {
    super("SALT LAKE COUNTY", "UT");
  }
  
  @Override
  public String getFilter() {
    return "4702193850,2083399349";
  }
  
  private static final Pattern MOVE_PTN = Pattern.compile("UNIT#:(\\S+) *, (Move from \\S+ *, To Post: \\S+) *, \\((.*)\\)");
  private static final Pattern MASTER_PTN1 = Pattern.compile("UNIT# *(\\S+) RUN # (\\S*?) *LOC:(.*?)APT ?#(.*?)BLDG:(.*?)ADD LOC:(.*?)CROSS ST:(.*?)TYPE: *(\\S*) *PRIORITY: *(.*?) *INIT BY:.*");
  private static final Pattern MASTER_PTN2 = Pattern.compile("UNIT#:(\\S+) RUN#(\\S+) *, LOC:(.*?)APT#:(.*?)BLDG:(.*), ADD LOC:(.*?)CROSS ST:(.*?), PAT.COND(\\S*) *PRIORITY:(.*?), INIT.BY:.*?, ZIP:(.*?) LAT/LON(.*?)");
  private static final Pattern GPS_PTN = Pattern.compile("([-+]?\\d{2,3})(\\d{6}) ([-+]?\\d{2,3})(\\d{6})");
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("UNIT#(\\S+) RUN#(\\S+) (RCVD:.*)");
  private static final Pattern RUN_REPORT_BRK = Pattern.compile("(?<=\\d\\d?:\\d\\d:\\d\\d) ");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MOVE_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT CALL ADDR");
      data.strUnit = match.group(1);
      data.strCall = match.group(2);
      data.strAddress = match.group(3).trim();
      return true;
    }
    
    if ((match = MASTER_PTN1.matcher(body)).matches()) {
      setFieldList("UNIT ID ADDR CITY APT PLACE INFO X CODE CALL PRI");
      data.strUnit = match.group(1);
      data.strCallId = match.group(2);
      parseAddress(match.group(3).trim(), data);
      data.strApt = append(data.strApt, "-", match.group(4).trim());
      data.strPlace = match.group(5).trim();
      data.strSupp = match.group(6).trim();
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT| FLAG_NO_CITY | FLAG_ANCHOR_END, match.group(7).trim(), data);
      data.strCode = match.group(8);
      String call = CALL_CODES.getCodeDescription(data.strCode);
      if (call == null) call = data.strCode;
      data.strCall = call;
      data.strPriority = match.group(9).trim();
      return true;
    }
    
    if ((match = MASTER_PTN2.matcher(body)).matches()) {
      setFieldList("UNIT ID ADDR CITY APT PLACE INFO X CODE CALL PRI GPS");
      data.strUnit = match.group(1);
      data.strCallId = match.group(2);
      parseAddressCity(match.group(3).trim(), data);
      data.strCity = convertCodes(data.strCity, CITY_CODES);
      data.strApt = append(data.strApt, "-", match.group(4).trim());
      data.strPlace = match.group(5).trim();
      data.strSupp = match.group(6).trim();
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT| FLAG_NO_CITY | FLAG_ANCHOR_END, match.group(7).trim(), data);
      data.strCode = match.group(8);
      String call = CALL_CODES.getCodeDescription(data.strCode);
      if (call == null) call = data.strCode;
      data.strCall = call;
      data.strPriority = match.group(9).trim();
      if (data.strCity.length() == 0) data.strCity = match.group(10).trim();
      String gps = match.group(11).trim();
      if (gps.length() > 0) {
        match = GPS_PTN.matcher(gps);
        if (match.matches()) {
          setGPSLoc(match.group(3)+'.'+match.group(4)+','+match.group(1)+'.'+match.group(2), data);
        }
      }
      return true;
    }
    
    if ((match = RUN_REPORT_PTN.matcher(body)).matches()) {
      setFieldList("UNIT ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strUnit = match.group(1);
      data.strCallId = match.group(2);
      data.strSupp = RUN_REPORT_BRK.matcher(match.group(3)).replaceAll("\n");
      return true;
    }
    return false;
  }
  
  private static final Pattern NORTH_TEMPLE_PTN = Pattern.compile("\\bNORTH TEMPLE\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern SOUTH_TEMPLE_PTN = Pattern.compile("\\bSOUTH TEMPLE\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern WEST_TEMPLE_PTN = Pattern.compile("\\bWEST TEMPLE\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = NORTH_TEMPLE_PTN.matcher(addr).replaceAll("N TEMPLE");
    addr = SOUTH_TEMPLE_PTN.matcher(addr).replaceAll("S TEMPLE");
    addr = WEST_TEMPLE_PTN.matcher(addr).replaceAll("W TEMPLE");
    return super.adjustMapAddress(addr);
  }
  
  private static CodeTable CALL_CODES = new StandardCodeTable();
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "SLC", "SALT LAKE CITY"
  });

}
