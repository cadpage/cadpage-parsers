package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class DispatchA81Parser extends MsgParser {
  
  public DispatchA81Parser(String defCity, String defState) {
    super(defCity, defState);
    setFieldList("CALL ID DATE TIME ADDR APT CITY ST");
  }
  
  private static final Pattern MASTER = Pattern.compile("([A-Z]{3}\\d{10}) (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)\\b *(.*?)(?: [A-Z]{2,4} - Assign .*)?");
  private static final Pattern STATE_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: (\\d{5}))");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    data.strCall = subject.length() > 0 ? subject : "ALERT";
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    String addr = match.group(4);
    
    Parser p = new Parser(addr);
    String city = p.getLastOptional(',');
    match = STATE_ZIP_PTN.matcher(city);
    if (match.matches()) {
      data.strState = match.group(1);
      data.strCity = match.group(2);
      city = p.getLastOptional(',');
    }
    data.strCity = city;
    parseAddress(p.get(), data);
    return true;
  }
}
