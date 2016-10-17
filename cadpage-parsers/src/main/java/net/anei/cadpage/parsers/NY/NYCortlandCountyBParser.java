package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchRedAlertParser;

public class NYCortlandCountyBParser extends DispatchRedAlertParser {
  
  public NYCortlandCountyBParser() {
    super("CORTLAND COUNTY", "NY");
  }
  
  @Override
  public String getFilter() {
    return "cortlandfd@rednmxcad.com";
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d) (\\d\\d/\\d\\d/\\d{4}) - .*");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    String[] flds = body.split("\n");
    if (flds.length == 1) {
      if (!super.parseMsg(subject, body, data)) return false;
      int pt = data.strAddress.indexOf(';');
      if (pt >= 0) {
        data.strPlace = append(data.strPlace, " - ", data.strAddress.substring(pt+1).trim());
        data.strAddress = data.strAddress.substring(0,pt).trim();
      }
      return true;
    }
    
    if (flds.length != 3) return false;
    
    setFieldList("CALL INFO DATE TIME ADDR APT");
    String line = flds[0].trim();
    int pt = line.indexOf(':');
    if (pt < 0) return false;
    data.strCall = line.substring(0,pt).trim();
    data.strSupp = line.substring(pt+1).trim();
    
    Matcher match = DATE_TIME_PTN.matcher(flds[1].trim());
    if (!match.matches()) return false;
    data.strTime = match.group(1);
    data.strDate = match.group(2);
    
    line = flds[2].trim();
    pt = line.indexOf(" at ");
    if (pt >= 0) {
      parseAddress(line.substring(pt+4).trim(), data);
      line = line.substring(0,pt).trim();
    }
    data.strSupp = append(data.strSupp, "\n", line);
    return true;
  }
}
