package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MOStLouisCountyAParser extends MsgParser {
  
  private static final Pattern MASTER_PTN = Pattern.compile("(.*?) \\(([A-Z /]+?)\\) -(.*?)\\b(\\d\\d:\\d\\d)$");

  public MOStLouisCountyAParser() {
    super("ST LOUIS COUNTY", "MO");
    setFieldList("PLACE ADDR APT CALL INFO UNIT CODE TIME");
  }
  
  @Override
  public String getFilter() {
    return "nccfas@ncc911.org,timparks@sbcglobal.net,ncfapaging@cce911.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
  
    Matcher match = MASTER_PTN.matcher(body);
    if (!match.matches()) return false;
    String address = match.group(1).trim();
    data.strCall = match.group(2).trim();
    body = match.group(3);
    data.strTime = match.group(4);

    int pt = address.indexOf(',');
    if (pt >= 0) {
      data.strPlace = address.substring(0,pt).trim();
      address = address.substring(pt+1).trim();
    }
    pt = address.indexOf(" BLDG ");
    if (pt >= 0) {
      data.strPlace = append(data.strPlace, " - ", address.substring(pt+6).trim());
      address = address.substring(0,pt).trim();
    }
    parseAddress(address, data);
    
    Parser p = new Parser(body);
    data.strSupp = p.get(" - ");
    data.strUnit = p.get(" - ");
    data.strCode = p.get();
    
    return true;
  }

}
