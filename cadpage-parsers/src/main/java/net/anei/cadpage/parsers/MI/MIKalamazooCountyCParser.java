package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;

public class MIKalamazooCountyCParser extends MsgParser {
  
  public MIKalamazooCountyCParser() {
    super("KALAMAZOO COUNTY", "MI");
    setFieldList("CALL ADDR APT CITY PLACE INFO");
  }
  
  @Override
  public String getFilter() {
    return "paging@kccda911.org";
  }
  
  private static final Pattern MASTER = Pattern.compile("([^@]+)@([^/,]+?)(?:,([^/,]*))?[/,](.*)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("!DISP")) return false;
    
    body = stripFieldEnd(body, "//");
    
    String extra = null;
    int pt = body.indexOf('\n');
    if (pt >= 0) {
      extra = stripFieldStart(body.substring(pt+1).trim(), "//");
      body = body.substring(0, pt).trim();
    }
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    String addr = match.group(2);
    parseAddress(addr.replace('@', '&'), data);
    data.strCity = getOptGroup(match.group(3));
    String place = stripFieldEnd(match.group(4).trim(), ",");
    if (place.equals(addr)) place = "";
    data.strPlace = place;
    
    if (extra != null) {
      for (String line : extra.split("\n")) {
        line = line.trim();
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
    return true;
  }
}
