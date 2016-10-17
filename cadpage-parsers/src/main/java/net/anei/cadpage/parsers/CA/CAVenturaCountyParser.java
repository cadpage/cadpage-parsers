package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Ventura County, CA
 */
public class CAVenturaCountyParser extends MsgParser {
  
  private static final Pattern TRAIL_PTN = Pattern.compile("(\\d{3}-[A-Z]\\d) +(\\d{1,2}) */ *(\\d{1,2}) *(\\d\\d-\\d{7})$");
  
  public CAVenturaCountyParser() {
    super("VENTURA COUNTY", "CA");
    setFieldList("UNIT CALL ADDR MAP CH ID");
  }
  
  @Override
  public String getFilter() {
    return "Fcc-do-not-reply@Ventura.org,Fcc@ventura.org,FCC-Page@ventura.org,FCC.Paging@ventura.org,fcc-paging@ventura.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    do {
      if (subject.equals("FCC Page")) break;
      
      if (body.startsWith("FCC Page / ")) {
        body = body.substring(11).trim();
        break;
      }
      return false;
    } while (false);
    
    if (!body.startsWith("Incident Dispatch:")) return false;
    
    // There are (at least) 2 fixed field formats.  We look for a distinctive
    // trailing signature to determine which one to use
    Matcher match = TRAIL_PTN.matcher(body);
    if (!match.find()) return false;
    
    data.strMap = match.group(1);
    data.strChannel = match.group(2) + '/' + match.group(3);
    data.strCallId = match.group(4);
    
    int len = match.start();
    if (len == 64) {
      data.strUnit = body.substring(18,24).trim();
      data.strCall = body.substring(24,34).trim();
      parseAddress(body.substring(34,64).trim(), data);
      return true;
    }
    
    if (len == 69) {
      data.strUnit = body.substring(20,26).trim();
      data.strCall = body.substring(27,37).trim();
      parseAddress(body.substring(38, 68).trim(), data);
      return true;
    }
    return false;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return BLOCK_PTN.matcher(addr).replaceAll("");
  }
  private static final Pattern BLOCK_PTN = Pattern.compile("[ -]?block", Pattern.CASE_INSENSITIVE);
}
