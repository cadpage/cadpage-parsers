package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class WVMarionCountyParser extends MsgParser {
  
  public WVMarionCountyParser() {
    super("MARION COUNTY", "WV");
    setFieldList("CALL ID DATE TIME ADDR APT CITY ST");
  }
  
  @Override
  public String getFilter() {
    return "marion911cad@gmail.com,marion911cad@marioncounty911.org,marion911@marioncountywv.com";
  }
  
  private static final Pattern MASTER = Pattern.compile("([A-Z]{3}\\d{10}) (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)\\b *(.*)");
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
