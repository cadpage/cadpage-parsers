package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class OHJeffersonCountyBParser extends MsgParser {

  public OHJeffersonCountyBParser() {
    super("JEFFERSON COUNTY", "OH");
    setFieldList("CALL DATE TIME ADDR CITY ST GPS INFO X");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }
  
  private static final Pattern MASTER = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) (.*?) ([-+]?\\d{2}\\.\\d{6} [-+]?\\d{2}\\.\\d{6}|None None)");
  private static final Pattern STATE_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private static final Pattern INFO_BRK_PTN = Pattern.compile(" \\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - ");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    data.strCall = subject;
    int pt = body.lastIndexOf(" Cross Streets:");
    if (pt >= 0) {
      data.strCross = body.substring(pt+15).trim();
      body = body.substring(0,pt).trim();
    }
    
    String[] lines = INFO_BRK_PTN.split(body);
    body = lines[0];
    if (lines.length == 1) {
      body = stripFieldEnd(body, "None");
    } else {
      for (int ndx = 1; ndx < lines.length; ndx++) {
        data.strSupp = append(data.strSupp, "\n", lines[ndx].trim());
      }
    }
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    String addr = match.group(3).trim();
    setGPSLoc(match.group(4), data);
    
    Parser p = new Parser(addr);
    String city = p.getLastOptional(',');
    if (city.length() > 0) {
      String zip = null;
      match = STATE_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        zip = match.group(2);
        city = p.getLastOptional(',');
      }
      if (city.length() == 0 && zip != null) city = zip;
      data.strCity = city;
    }
    parseAddress(p.get(), data);
    
    return true;
  }
}
