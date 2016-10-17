package net.anei.cadpage.parsers.DE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.StandardCodeTable;



public class DEWilmingtonBParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("([A-Z0-9]+) (?:Tactical )?Box Alarm (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) (?:(.*?) (\\d{1,2}[A-Z]\\d{1,2}[A-Z]?)\\b *(.*)|(.*))");
  
  public DEWilmingtonBParser() {
    super("WILMINGTON", "DE");
    setFieldList("BOX DATE TIME ADDR APT CODE CALL INFO");
  }
  
  @Override
  public String getFilter() {
    return "WLPD@state.de.us";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Incident")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strBox = match.group(1);
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    String addr = match.group(4);
    if (addr != null) {
      parseAddress(addr.trim(), data);
      data.strCode = match.group(5);
      data.strSupp = match.group(6);
      
      String call = CALL_CODES.getCodeDescription(data.strCode);
      if (call == null) call = data.strCode;
      data.strCall = call;
      return true;
    }
    
    else {
      parseAddress(StartType.START_ADDR, match.group(7).trim(), data);
      data.strCall = getLeft();
      return (getStatus() > STATUS_STREET_NAME);
    }
  }
  
  private static final CodeTable CALL_CODES = new StandardCodeTable();
}


