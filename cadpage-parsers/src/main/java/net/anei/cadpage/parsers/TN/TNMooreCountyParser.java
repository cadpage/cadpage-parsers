package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class TNMooreCountyParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("^(\\d\\d?\\-\\d\\d?\\-\\d\\d) (\\d{2}\\:\\d{2}) Incident Alert\\: (.+?) \\- (.*)");
  private static final Pattern MUTUAL_AID_TRAILER = Pattern.compile("(.*)\\-\\s*MUTUAL AID");
  public TNMooreCountyParser() {
    super("MOORE COUNTY", "TN");
    setFieldList("DATE TIME CALL ADDR");
  }
  
  public String getFilter() {
    return "noreply@emergencycallworx.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {    
    Matcher match = MASTER.matcher(body);
    String address;
    if (match.matches()) {
      data.strDate = match.group(1);
      data.strDate = data.strDate.replace("-", "/");
      data.strTime = match.group(2);
      data.strCall = match.group(3).trim();
      address = match.group(4).trim();
      match = MUTUAL_AID_TRAILER.matcher(address);
      if (match.matches()) {
        address = match.group(1).trim();
        data.strCall += " - MUTUAL AID";
      }
      parseAddress(address, data);
      return true;
    }
    return false;
  }
}
