package net.anei.cadpage.parsers.AR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARPulaskiCountyCParser extends SmartAddressParser {
  
  public ARPulaskiCountyCParser() {
    super("PULASKI COUNTY", "AR");
    setFieldList("ID CALL ADDR PLACE APT CITY ST CH INFO");
  }
  
  @Override
  public String getFilter() {
    return "smtp@pcso.org";
  }
  
  private static final Pattern ID_PTN = Pattern.compile("CFS\\d{4}-\\d{5}");
  private static final Pattern CH_PTN = Pattern.compile("(\\d+|[A-Z]{2,4} (?:FIRE )?\\d+|ONE)\\b *(.*)");
  private static final Pattern CANCEL_PTN = Pattern.compile("(.*) (CANCEL(?: RESPONSE)?)");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: (\\d{5}))?");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!ID_PTN.matcher(subject).matches()) return false;
    data.strCallId = subject;
    
    String cancel = "";
    int pt = body.indexOf(" Fire Channel");
    if (pt >= 0) {
      String channel = body.substring(pt+13).trim();
      body = body.substring(0,pt).trim();
      Matcher match = CH_PTN.matcher(channel);
      if (match.matches()) {
        data.strChannel = match.group(1);
        channel = match.group(2);
      }
      data.strSupp = stripFieldStart(channel, "/");
    }
    
    else {
      Matcher match = CANCEL_PTN.matcher(body);
      if (!match.matches()) return false;
      body = match.group(1).trim();
      cancel = match.group(2);
    }
    
    Parser p = new Parser(body);
    data.strCall = append(cancel, " - ", p.get(','));
    String addr = p.get(',');
    if (addr.length() == 0) return false;
    parseAddress(addr.replace('@',  '&'), data);
    
    String city = p.getLast(',');
    Matcher match = ST_ZIP_PTN.matcher(city);
    if (match.matches()) {
      data.strState = match.group(1);
      String zip = match.group(2);
      city = p.getLast(',');
      if (city.isEmpty() && zip != null) city = zip;
    }
    data.strCity = city;
    return p.get().isEmpty();
  }
}
