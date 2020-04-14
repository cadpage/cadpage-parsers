package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class NJSalemCountyCParser extends MsgParser {
  
  public NJSalemCountyCParser() {
    this("SALEM COUNTY", "NJ");
  }
  
  public NJSalemCountyCParser(String defCity, String defState) {
    super(defCity, defState);
    setFieldList("ID CALL PLACE ADDR APT CITY ST UNIT INFO");
  }
  
  @Override
  public String getAliasCode() {
    return "NJSalemCountyC";
  }
  
  @Override
  public String getFilter() {
    return "E_Messaging@salemcountynj.gov";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("[A-Z}]{2,3}-\\d{4}-\\d{5,6}");
  private static final Pattern MASTER = Pattern.compile("([-;/ A-Z]+) @ (.*?), (.*?)(?: \n(.*))?", Pattern.DOTALL);
  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("([ A-Z]+) (NJ|DE)\\b *\\d{0,5}");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!SUBJECT_PTN.matcher(subject).matches()) return false;
    data.strCallId = subject;

    body = stripFieldEnd(body, "...");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    String addr = match.group(2).trim();
    String cityStZip = match.group(3).trim();
    String left = match.group(4);
    
    int pt = addr.indexOf(" - ");
    if (pt >= 0) {
      data.strPlace = addr.substring(0, pt).trim();
      addr = addr.substring(pt+3).trim();
    }
    parseAddress(addr, data);
    
    match = CITY_ST_ZIP_PTN.matcher(cityStZip);
    if (match.matches()) {
      data.strCity = stripFieldEnd(match.group(1).trim(), " BORO");
      data.strState = match.group(2);
    }
    
    if (left != null) {
      if (left.startsWith("Active Units:")) {
        left = left.substring(13).trim();
        pt = left.indexOf(" - ");
        if (pt >= 0) {
          data.strUnit = left.substring(0, pt).trim();
          data.strSupp = left.substring(pt+3).trim();
        } else {
          data.strUnit = stripFieldEnd(left, " -");
        }
      }
      else if (!"Active Units:".startsWith(left)) return false;
    }
    
    return true;
  }
}
