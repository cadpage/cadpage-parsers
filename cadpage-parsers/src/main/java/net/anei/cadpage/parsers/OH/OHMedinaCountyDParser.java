package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class OHMedinaCountyDParser extends MsgParser {
  
  public OHMedinaCountyDParser() {
    super("MEDINA COUNTY", "OH");
    setFieldList("ADDR APT CITY ST CALL INFO");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@medinaco.org";
  }
  
  private static final Pattern LOG_HEADER_PTN = Pattern.compile(";? +\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - log - ");
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("([^,]+)(?:, *([A-Z ]+))??(?:, *([A-Z]{2})(?: +(\\d{5}))?)?");  
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    data.strCall = subject;
    
    String[] flds = LOG_HEADER_PTN.split(body);
    if (flds.length > 1) {
      body = flds[0].trim();
      for (int jj = 1; jj < flds.length; jj++) {
        data.strSupp = append(data.strSupp, "\n", flds[jj].trim());
      }
    } else {
      if (!body.endsWith(" None;")) return false;
      body = body.substring(0, body.length()-6).trim();
    }
    
    int pt = body.indexOf("; ");
    if (pt >= 0) {
      data.strCall = body.substring(pt+2).trim();
      body = body.substring(0, pt).trim();
    } else {
      if (!body.endsWith(";")) return false;
      body = body.substring(0, body.length()-1).trim();
    }
    
    Matcher match = ADDR_ST_ZIP_PTN.matcher(body);
    if (!match.matches()) return false;
    parseAddress(match.group(1).trim(), data);
    data.strCity = getOptGroup(match.group(2));
    data.strState = getOptGroup(match.group(3));
    String zip = match.group(4);
    if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    return true;
  }
}
