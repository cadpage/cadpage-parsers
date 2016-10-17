package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA6Parser extends SmartAddressParser {
  
  public DispatchA6Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState);
  }
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("->Inc Addr = (.*?)Equip = (.*?)(Create = .*)Rpt # = *([-0-9]*)");
  private static final Pattern RUN_REPORT_TIME = Pattern.compile(" *=(?: (\\d\\d? \\d\\d \\d\\d))?");
  private static final Pattern LEAD_DATE_PAT = Pattern.compile("^(?:([- A-Z0-9]+) )?(?:\\d\\d/\\d\\d/\\d{4} T?ARTADDR# )?(?:(?:Ic# +(.*?) +Ds# ([^ ]*?) (?:Al# ([^ ]*?)|Cntrl)? Utl# (?:([- A-Z0-9]*?) +)?)?(\\d\\d/\\d\\d/\\d\\d(?:\\d\\d)?)|\\^ ) *-? *");
  private static final Pattern CROSS_MARK2_PAT = Pattern.compile(" :\\\\?\\( *(\\d*) *\\) | <> ");
  private static final Pattern CROSS_MARK1_PAT = Pattern.compile("^\\{ *\\d+ *\\} *");
  private static final Pattern TIME_UNIT_PAT = Pattern.compile("(?<!\\d)(\\d{4}),(\\d{3})");
 
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    
    // Check for a run report pattern
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ADDR APT UNIT INFO ID");
      data.msgType = MsgType.RUN_REPORT;
      parseAddress(match.group(1).trim(), data);
      data.strUnit = match.group(2).trim();
      String times = match.group(3).trim();
      data.strCallId = match.group(4);
      
      match = RUN_REPORT_TIME.matcher(times);
      StringBuffer sb = new StringBuffer();
      while (match.find()) {
        String time = match.group(1);
        if (time == null) time = "\n";
        else time = "  "+time.replace(' ', ':')+'\n';
        match.appendReplacement(sb, time);
      }
      match.appendTail(sb);
      data.strSupp = sb.toString();
      return true;
    }
    
    setFieldList("CALL ID MAP DATE ADDR APT CITY PLACE ID X INFO TIME UNIT");
    match = LEAD_DATE_PAT.matcher(body);
    if (!match.find()) return false;
    body = body.substring(match.end());
    data.strCall = getOptGroup(match.group(1));
    String call = match.group(2);
    if (call != null) {
      data.strCall = call;
      data.strCallId = append(match.group(3), "-", getOptGroup(match.group(4)));
      data.strMap = getOptGroup(match.group(5));
    }
    if (data.strDate.length() == 0) data.strDate = getOptGroup(match.group(6));
    
    match = CROSS_MARK2_PAT.matcher(body);
    if (!match.find()) return false;
    String crossNumber = match.group(1);
    
    String sAddr = body.substring(0,match.start()).trim();
    body = body.substring(match.end()).trim();
    
    boolean intersect = false;
    match = CROSS_MARK1_PAT.matcher(sAddr);
    if (match.find()) {
      intersect = true;
      sAddr = sAddr.substring(match.end());
    }
    
    int pt = sAddr.lastIndexOf('#');
    if (pt >= 0) {
      data.strPlace = sAddr.substring(pt+1).trim();
      sAddr = sAddr.substring(0,pt).trim();
    }
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, sAddr, data);
    
    match = TIME_UNIT_PAT.matcher(body);
    if (match.find()) {
      String time = match.group(1);
      if (data.strTime.length() == 0) data.strTime = time.substring(0,2) + ":" + time.substring(2);
      if (data.strUnit.length() == 0) data.strUnit = match.group(2);
      
      body = match.replaceAll(" / ").replaceAll("  +", " ").trim();
      body = stripFieldEnd(body, "/");
    }
    
    Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, body);
    if (res.isValid()) {
      res.getData(data);
      data.strSupp = res.getLeft();
      if (intersect) {
        data.strAddress = append(data.strAddress, " & ", data.strCross);
        data.strCross = "";
      } else if (crossNumber != null) {
        data.strCross = append(crossNumber, " ", data.strCross);
      }
    } else {
      data.strSupp = body;
    }
    
    return true;
  }
}
