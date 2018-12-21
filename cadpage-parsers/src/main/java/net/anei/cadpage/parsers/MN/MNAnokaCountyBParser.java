package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MNAnokaCountyBParser extends MsgParser {
  
  public MNAnokaCountyBParser() {
    super("ANOKA COUNTY", "MN");
    setFieldList("ID CODE CALL ADDR APT PLACE CITY MAP INFO SRC");
  }
  
  @Override
  public String getFilter() {
    return "InformCAD@paging.acw-psds.org";
  }
  
  private static final Pattern LEAD_ID_PTN = Pattern.compile("[A-Z]{2}\\d{8}");
  private static final Pattern INFO_SKIP_PTN = Pattern.compile("\\[\\d\\]");
  private static final Pattern TRAIL_ID_PTN = Pattern.compile("(.*)(\\d{2}[A-Z]-[A-Z]{2}\\d{4})");
  private static final Pattern ID_PTN = Pattern.compile("\\d{11}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD Paging")) return false;
    
    FParser p = new FParser(body);
    
    do {
      if (p.check("FIRE/RESCUE PAGE:")) {
        String id = p.lookahead(0, 10);
        if (LEAD_ID_PTN.matcher(id).matches()) {
          data.strCallId = id;
          p.skip(10);
        }
        break;
      }
      
      if (p.check("Active 911:")) {
        data.strCallId = p.get(10);
        break;
      }
      
      String id = p.get(11);
      if (ID_PTN.matcher(id).matches() && p.checkBlanks(2)) {
        data.strCallId =  id;
        break;
      }
      
      return false;
    } while (false);
    
    if (p.check(" ")) return false;
    String call = p.get(30);
    int pt = call.indexOf('-');
    if (pt >= 0) {
      data.strCode = call.substring(0,pt).trim();
      call = call.substring(pt+1).trim();
    }
    data.strCall = call;
    
    parseAddress(p.get(40), data);
    
    String apt = p.get(5);
    if (apt.startsWith("#")) apt = apt.substring(1).trim();
    data.strApt = append(data.strApt, "-", apt);
    
    data.strPlace = p.get(20);
    data.strCity = p.get(20);
    
    if (!p.check("[1] ")) {
      data.strMap = p.get(10);
      if (data.strMap.equals("NOT FOUND")) data.strMap = "";
      p.check("[1] ");
    }
    
    String info = p.get(496);
    data.strSource = p.get();
    info = INFO_SKIP_PTN.matcher(info).replaceAll("").trim();
    if (data.strCallId.length() == 0) {
      Matcher match = TRAIL_ID_PTN.matcher(info);
      if (match.matches()) {
        info = match.group(1).trim();
        data.strCallId = append(data.strCallId, "/", match.group(2));
      }
    }
    data.strSupp = info;
    return true;
  }
}
