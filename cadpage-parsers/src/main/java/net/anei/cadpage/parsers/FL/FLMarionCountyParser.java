package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.MsgParser;

public class FLMarionCountyParser extends MsgParser {
  
  public FLMarionCountyParser() {
    super("MARION COUNTY", "FL");
  }
  
  @Override
  public String getFilter() {
    return "Cad.Paging@marioncountyfl.org,@tcr.readyalert.com";
  }
  
  private static final Pattern MASTER2 = Pattern.compile("/ ([^/]+) / (?:From )?([A-Za-z0-9 ]+) (\\d\\d:\\d\\d) EST (.*)");
  private static final Pattern ALERT_PREFIX_PTN = Pattern.compile("\\**([A-Za-z0-9 ]+)\\*+ *([A-Za-z0-9]+) *#(\\d{4}-\\d{6}) +(.*)");
  private static final Pattern ALERT_INFO_PTN = Pattern.compile("(?:ref:)?(.*?)address:(.*?)apt/lot/rm#:(.*?)bldg/wing:(.*?)zip (.*?) map page (?:pg *(\\S*)|not found) *.*");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {3,}");
  
  private static final Pattern MASTER1 = Pattern.compile("\\*{3}CALL ALERT\\*{3} (\\S+) Case #(\\d{4}-\\d{6}) Ref:(.*?)Address:(.*?)Loc Name:(.*?)Apt/Lot/Rm#:(.*?)Bldg/Wing:(.*?)Zip (\\d*) Map Page pg +(\\S*)(?: *.*)?");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldEnd(body, ",");
    
    Matcher match = MASTER2.matcher(body);
    if (match.matches()) {
      data.strCall = match.group(1).trim();
      data.strSource = match.group(2).trim();
      data.strTime = match.group(3);
      body = match.group(4).trim();
      
      match = ALERT_PREFIX_PTN.matcher(body);
      if (match.matches()) {
        data.strCall = match.group(1).trim();
        data.strUnit = match.group(2);
        data.strCallId = match.group(3);
        body = match.group(4);
        
        match = ALERT_INFO_PTN.matcher(body);
        if (match.matches()) {
          setFieldList("SRC TIME CALL UNIT ID ADDR APT CITY MAP");
          data.strCall = append(data.strCall, " - ", match.group(1).trim());
          String addr = match.group(2).trim();
          int pt = addr.indexOf("   ");
          if (pt >= 0) {
            data.strPlace = addr.substring(pt+3).trim();
            addr = addr.substring(0,pt);
          }
          parseAddress(addr, data);
          String apt = append(match.group(3).trim(), "-", match.group(4).trim());
          data.strApt = append(data.strApt, "-", apt);
          data.strCity = match.group(5).trim();
          data.strMap = getOptGroup(match.group(6));
          return true;
        }
        
        if (body.startsWith("cancel reason:")) {
          setFieldList("SRC TIME CALL UNIT ID INFO");
          data.msgType = MsgType.RUN_REPORT;
          data.strSupp = MBLANK_PTN.matcher(body).replaceAll("\n"); 
          return true;
        }
      }
      setFieldList("CALL SRC TIME UNIT ID INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body;
      return true;
    }
    
    body = body.replaceAll("  +", " ");
    match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT ID CALL ADDR PLACE APT CITY MAP");
      data.strUnit = match.group(1);
      data.strCallId = match.group(2);
      data.strCall = match.group(3).trim();
      parseAddress(match.group(4).trim(), data);
      data.strPlace = match.group(5).trim();
      String apt = append(match.group(7).trim(), "-", match.group(6).trim());
      data.strApt = append(data.strApt, "-", apt);
      data.strCity = match.group(8);
      data.strMap = match.group(9).trim();
      return true;
    }
    
    return false;
  }

}
