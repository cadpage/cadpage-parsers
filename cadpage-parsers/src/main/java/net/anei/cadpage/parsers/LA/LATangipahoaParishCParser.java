package net.anei.cadpage.parsers.LA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class LATangipahoaParishCParser extends MsgParser {
  
  public LATangipahoaParishCParser() {
    super("TANGIPAHOA PARISH", "LA");
    setFieldList("SRC CALL ADDR APT CITY ST DATE TIME INFO");
  }

  @Override
  public String getFilter() {
    return "pointecoupee@pagingpts.com";
  }
  
  private static final Pattern SRC_CALL_PTN = Pattern.compile("([A-Z]+) +(.*)");
  private static final Pattern MASTER = Pattern.compile("([^,]*), ([A-Za-z ]*), ([A-Z]{2}) (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d [AP]M)\\b *(.*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    if (!body.startsWith(subject)) return false;
    body = body.substring(subject.length()).trim();
    
    Matcher match = SRC_CALL_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    data.strCall = match.group(2);
    
    match = MASTER.matcher(body);
    if (!match.matches()) return false;
    String addr = match.group(1).trim();
    String apt = null;
    int pt = addr.indexOf(" APT ");
    if (pt >= 0) {
      apt = addr.substring(pt+5).trim();
      addr = addr.substring(0, pt).trim();
    }
    parseAddress(addr, data);
    if (apt != null) data.strApt = append(data.strApt, "-", apt);
    
    data.strCity = match.group(2).trim();
    data.strState = match.group(3).trim();
    data.strDate = match.group(4);
    setTime(TIME_FMT, match.group(5), data);
    data.strSupp = match.group(6);
    return true;
  }
}
