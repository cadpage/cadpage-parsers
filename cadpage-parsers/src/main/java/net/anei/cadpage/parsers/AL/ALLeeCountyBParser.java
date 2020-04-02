package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALLeeCountyBParser extends DispatchA71Parser {
  
  public ALLeeCountyBParser() {
    super("LEE COUNTY", "AL");
    setupCities(ALLeeCountyParser.CITY_LIST);
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("\\d+-CFS Report (\\d{4}-\\d+) ([^,]+), (.*)");
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d\\d-\\d\\d) +(.*)"); 
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = RUN_REPORT_PTN.matcher(subject);
    if (match.matches()) {
      setFieldList("ID ADDR APT CITY CALL");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      parseAddress(match.group(2).trim(), data);
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, match.group(3).trim(), data);
      data.strCall = getLeft();
      return true;
    }
    
    if (!super.parseMsg(body, data)) return false;
    match = CODE_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }
    data.strCross = data.strCross.replace('@',  '/');
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }
}
