package net.anei.cadpage.parsers.NJ;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


/**
 * JCMC EMS HudCEN, NJ
 */
public class NJJCMCEMSJerseyCityParser extends MsgParser {
  
  public NJJCMCEMSJerseyCityParser() {
    super("HUDSON COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "hudcen@libertyhcs.org,HUDCEN@barnabashealth.org,hudcen@rwjbh.org";
  }
  
  @Override
  public String getLocName() {
    return "JCMC EMS Jersey City, NJ";
  }
  
  private static final Pattern MASTER1 = Pattern.compile("Closest Hospital/Med Ctr to (.*?): (.*)");
  private static final Pattern BREAK_PTN = Pattern.compile(" *\\| *");
  
  private static final Pattern TIME_PTN = Pattern.compile("(.*?) +(\\d\\d?:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
  
  private static final Pattern CANCEL_PTN = Pattern.compile("Call Cancelled: (.* Cancelled:[\\d:]*) #([-0-9]+)");
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("Times: (.*?) +Response Number:([-0-9]+)");
  private static final Pattern MASTER_PTN = Pattern.compile("Unit:(\\d+) +(.*?) Resp:(.*?) Apt\\.(.*?) /(.*?)S?Cross:(.*)");
  private static final Pattern APT_PLACE_PTN = Pattern.compile("(.+?) +(?!FRONT|REAR|FLR?\\b|\\d+)(.*)", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean parseMsg(String body, Data data) {
    
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ADDR INFO");
      data.strCall = "ALERT";
      parseAddress(match.group(1).trim(), data);
      data.strSupp = BREAK_PTN.matcher(match.group(2).trim()).replaceAll("\n");
      return true;
    }
    
    match = TIME_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
    match = CANCEL_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CALL PLACE ID TIME");
      data.strCall = "Call Cancelled";
      data.strPlace = match.group(1).trim();
      data.strCallId = match.group(2);
      return true;
    }
    
    match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      data.strCall = "RUN REPORT";
      data.strPlace = match.group(1);
      data.strCallId = match.group(2);
      return true;
    }
    
    match = MASTER_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT CITY ADDR APT CALL X TIME");
      data.strUnit = match.group(1);
      data.strCity = match.group(2).trim();
      parseAddress(match.group(3).trim(), data);
      String apt = match.group(4).trim();
      data.strCall = match.group(5).trim();
      if (data.strCall.length() == 0) data.strCall = "ALERT";
      data.strCross = match.group(6).trim();
      
      match = APT_PLACE_PTN.matcher(apt);
      if (match.matches()) {
        apt = match.group(1);
        data.strPlace = match.group(2);
      }
      data.strApt = append(data.strApt, "-", apt);
      return true;
    }
    
    return false;
  }

}
