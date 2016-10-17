package net.anei.cadpage.parsers.IL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class ILCookCountyDParser extends MsgParser {
  
  public ILCookCountyDParser() {
    super("COOK COUNTY", "IL");
    setFieldList("CALL TIME UNIT ADDR APT CITY PLACE INFO ID");
  }
  
  @Override
  public String getFilter() {
    return "911@nwcds.org";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("([-/ A-Z0-9]+)\\|(\\d\\d:\\d\\d:\\d\\d)");
  private static final Pattern MASTER = Pattern.compile("([A-Z0-9]+) at ([^,]*?), ([A-Z]{2}) - ([^,]*), (.*?)\\. \\$([A-Z]{3}\\d{8})\\. (\\d{8})\\.", Pattern.DOTALL);
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    data.strTime = match.group(2);
    
    match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = match.group(1);
    parseAddress(match.group(2).trim(), data);
    data.strCity = convertCodes(match.group(3), CITY_CODES);
    data.strPlace = match.group(4).trim();
    data.strSupp = match.group(5).trim();
    data.strCallId = match.group(6) + '/' + match.group(7);
    return true;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AH", "ARLINGTON HEIGHTS",
      "BG", "BUFFALO GROVE",
      "LG", "LONG GROVE"
  });
}
