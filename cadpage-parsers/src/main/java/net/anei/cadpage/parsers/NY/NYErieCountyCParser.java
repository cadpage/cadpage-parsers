package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class NYErieCountyCParser extends SmartAddressParser {
  
  private static final Pattern MARKER = Pattern.compile("^(?:Erie_Alert|USMO SMS:) ");
  private static final Pattern TRAIL_JUNK = Pattern.compile("[\\. ]+$");
  
  public NYErieCountyCParser() {
    super(CITY_LIST, "ERIE COUNTY", "NY");
    setFieldList("CALL ADDR APT CITY INFO X PLACE");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    body = body.substring(match.end()).trim();
    if (body.endsWith("<NOREPLY>")) body = body.substring(0,body.length()-9).trim();
    body = TRAIL_JUNK.matcher(body).replaceAll("");
    
    String saveBody = body;
    
    int pt = body.indexOf(" c/s:");
    if (pt >= 0) {
      data.strCross = body.substring(pt+5).trim();
      body = body.substring(0,pt).trim();
    }
    
    parseAddress(StartType.START_CALL, FLAG_IGNORE_AT | FLAG_NO_IMPLIED_APT, body, data);
    data.strSupp = getLeft();
    if (!isValidAddress()) {
      return data.parseGeneralAlert(this, saveBody);
    }
    if (data.strCall.length() == 0) {
      if (data.strSupp.length() > 0 && data.strSupp.length() <= 40) {
        data.strCall = data.strSupp;
        data.strSupp = "";
      } else {
        data.strCall = "ALERT";
      }
    }
    
    pt = data.strCall.indexOf(':');
    if (pt >= 0) {
      data.strSupp = append(data.strCall.substring(pt+1).trim(), " / ", data.strSupp);
      data.strCall = data.strCall.substring(0,pt).trim();
    }
    
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    "EDEN",
    "KENMORE",
    "TONAWANDA TOWN",
    "WEST SENECA"
  };
}
