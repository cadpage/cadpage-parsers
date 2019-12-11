package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;

public class GAStewartCountyParser extends SmartAddressParser {
  
  public GAStewartCountyParser() {
    super(CITY_LIST, "STEWART COUNTY", "GA");
    setFieldList("SRC ID CODE DATE TIME CALL ADDR APT CITY NAME GPS");
  }
  
  @Override
  public String getFilter() {
    return "earlyga@ez911mail.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("([A-Z]+) +(\\d+)");
  private static final Pattern MASTER = Pattern.compile("(.*?)  Date Recv (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M) (.*?)  http://maps.google.com/maps\\?q=(.*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    data.strCallId = match.group(2);
    
    match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCode = match.group(1).trim();
    data.strDate =  match.group(2);
    setTime(TIME_FMT, match.group(3), data);
    
    parseAddress(StartType.START_CALL, match.group(4).trim(), data);
    data.strName = getLeft();
    
    setGPSLoc(match.group(5).replace('+', ' '), data);
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
      "GREEN GROVE",
      "LOUVALE",
      "LUMPKIN",
      "OMAHA",
      "RICHLAND",
      "BROOKLYN",
      "FLORENCE"
  };
}
