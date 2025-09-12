package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class MOClintonCountyAParser extends MsgParser {
  
  public MOClintonCountyAParser() {
    super("CLINTON COUNTY", "MO");
    setFieldList("CALL CODE ADDR APT CITY ST PLACE INFO");
  }
  
  @Override
  public String getFilter() {
    return "centralsquare@cameronmo.com";  
  }
  
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]+\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private static final Pattern MASTER = Pattern.compile("(?:([A-Z]+) )?None +(.*)");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +\\d{5})?");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.isEmpty()) return false;
    data.strCall = subject;
    
    String[] parts = INFO_BRK_PTN.split(body);
    body = parts[0];
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCode = getOptGroup(match.group(1));
    body =  match.group(2);
    
    String addr;
    int brk = findDuplicate(body);
    if (brk >= 0) {
      addr = body.substring(0, brk).trim();
      data.strPlace = body.substring(brk+addr.length()).trim();
    } else return false;
    
    Parser p = new Parser(addr);
    String city = p.getLastOptional(',');
    match = ST_ZIP_PTN.matcher(city);
    if (match.matches()) {
      data.strState = match.group(1);
      city = p.getLastOptional(',');
    }
    parseAddress(p.get(), data);
    data.strCity = city;
    
    for (int j = 1; j < parts.length; j++) {
      data.strSupp = append(data.strSupp, "\n", parts[j]);
    }
    return true;
  }
  
  private int findDuplicate(String field) {
    int k = (field.length()+1)/2;
    for (int j = k; j > 0; j--) {
      if (field.charAt(j) != ' ') k = j;
      if (field.charAt(j) == ' ' && field.charAt(j-1) != ' ') {
        if (field.substring(k).startsWith(field.substring(0, j))) return k;
      }
    }
    return -1;
  }
}
