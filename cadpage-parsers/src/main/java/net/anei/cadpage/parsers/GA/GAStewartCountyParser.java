package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class GAStewartCountyParser extends SmartAddressParser {
  
  public GAStewartCountyParser() {
    super(CITY_LIST, "STEWART COUNTY", "GA");
    setFieldList("SRC ID CODE DATE TIME CALL ADDR APT CITY NAME GPS INFO");
    setupMultiWordStreets("CHARLES OVERBY");
  }
  
  @Override
  public String getFilter() {
    return "earlyga@ez911mail.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }


  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:([A-Z]+) )?+(\\d+)");
  private static final Pattern ALERT_MASTER = Pattern.compile("(.*?) +Date Recv (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M) (.*?)(?: +http://maps.google.com/maps\\?q=(.*))?");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(.*?) (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M) (.*?) (Case # \\d+ */.*)");
  private static final Pattern TIMES_BRK_PTN = Pattern.compile(" +/(?=[ A-Za-z]+ - )");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = getOptGroup(match.group(1));
    data.strCallId = match.group(2);
      
    match = ALERT_MASTER.matcher(body);
    if (match.matches()) {
      setFieldList("SRC ID CODE DATE TIME CALL ADDR APT CITY NAME GPS");
      data.strCode = match.group(1).trim();
      data.strDate =  match.group(2);
      setTime(TIME_FMT, match.group(3), data);
      
      parseAddress(StartType.START_CALL, match.group(4).trim(), data);
      data.strName = getLeft();
    
      String gps = match.group(5);
      if (gps != null) setGPSLoc(gps.replace('+', ' '), data);
      else data.expectMore = true;
      return true;
    }
    
    if ((match = RUN_REPORT_PTN.matcher(body)).matches()) {
      setFieldList("SRC ID CODE DATE TIME PLACE ADDR APT CITY CALL INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCode = match.group(1);
      data.strDate =  match.group(2);
      setTime(TIME_FMT, match.group(3), data);
      
      parseAddress(StartType.START_PLACE, match.group(4).trim(), data);
      data.strCall = getLeft();
      if (data.strSupp.equals("None")) data.strSupp = "";
      String times = match.group(5);
      data.strSupp = TIMES_BRK_PTN.matcher(times).replaceAll("\n");
      
      if (data.strPlace.length() > 0 && data.strAddress.length() == 0) {
        String place = PLACE_SET.getCode(data.strPlace);
        if (place != null) {
          data.strAddress = place;
          data.strCall = data.strPlace.substring(place.length()).trim();
        } else {
          data.strAddress = data.strPlace;
        }
        data.strPlace = "";
        
      }
      return true;
    }
    
    return false;
  }
  
  private static final CodeSet PLACE_SET = new CodeSet(
      "FOUR COUNTY HEALTH & REHAB",
      "HOUSE",
      "PROVIDENCE FAMILY MEDICAL"  
  );
  
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
