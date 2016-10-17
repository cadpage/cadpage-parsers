package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class MNWashingtonCountyParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("(?:(CG\\d+) +)?(.*?)  ICR #(\\d+)  (?:TYPE:(.*?)  )?(DISPATCH)(?: (\\d\\d:\\d\\d:\\d\\d))?");
  private static final Pattern HWY_I_PTN = Pattern.compile("\\bHWY +I(\\d+)\\b");
  
  public MNWashingtonCountyParser() {
    super("WASHINGTON COUNTY", "MN");
    setFieldList("UNIT ADDR APT ID CALL TIME");
  }
  
  @Override
  public String getFilter() {
    return "sheriff@co.washington.mn.us,40404";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    do {
      if (subject.equals("ICOM/400 notification")) break;
      
      if (body.startsWith("@CGFDDispatch: ")) {
        body = body.substring(15);
        int pt = body.indexOf('\n');
        if (pt >= 0) body = body.substring(0,pt);
        body = body.trim();
        break;
      }
      
      return false;
    } while (false);
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    data.strUnit = getOptGroup(match.group(1));
    String addr = match.group(2).replace('.', ' ').trim();
    addr = HWY_I_PTN.matcher(addr).replaceAll("I$1");
    parseAddress(StartType.START_ADDR, addr, data);
    String apt = getLeft();
    if (apt.startsWith("/")) {
      data.strAddress = data.strAddress + " & " + apt.substring(1).trim();
    } else if ((data.strAddress.endsWith(" ST") || data.strAddress.endsWith(" DR")) &&
               (apt.startsWith("PL ") || apt.startsWith("LN "))) {
      data.strAddress = data.strAddress + ' ' + apt;
    }  else {
      data.strApt = append(data.strApt, " ", apt);
    }
    data.strCallId = match.group(3);
    data.strCall = getOptGroup(match.group(4));
    if (data.strCall.length() == 0) data.strCall = match.group(5);
    data.strTime = getOptGroup(match.group(6));
    return true;
  }
}
