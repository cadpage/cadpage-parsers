package net.anei.cadpage.parsers.WI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class WIDaneCountyParser extends SmartAddressParser {

  public WIDaneCountyParser() {
    super("DANE", "WI");
    setFieldList("CITY PLACE ADDR X PRI CODE CALL UNIT");
  }
  
  private static Pattern MASTER = Pattern.compile("Response Info Page: Municipality [A-Z]+-(.*?) (?:City|Town|Village)(?: (.*))? At +(.*?) +cross of(?: +(.*?))? +a +(.*?) +response for[ \\.]+(?:(\\d\\w+)-)?(.*?) *\\. Units assigned to alarm: +(.*)");
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("Paging Message from VisiCAD")) return false;
    
    //parse in one swoop
    Matcher mat = MASTER.matcher(body);
    if (!mat.matches()) return false;
    
    //i think i need to change le pattern or trim all of these
    data.strCity = mat.group(1).trim();
    data.strPlace = getOptGroup(mat.group(2));
    parseAddress(mat.group(3).trim(), data);
    data.strCross = getOptGroup(mat.group(4));
    data.strPriority = mat.group(5);
    data.strCode = getOptGroup(mat.group(6));
    data.strCall = mat.group(7);
    data.strUnit = mat.group(8);
    
    //CLean up cross street info
    data.strCross = stripFieldStart(data.strCross, "NO X STREET");
    data.strCross = stripFieldStart(data.strCross, "/");
    data.strCross = stripFieldEnd(data.strCross, "NO X STREET");
    data.strCross = stripFieldEnd(data.strCross, "/");
    
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = USH_PTN.matcher(addr).replaceAll("US");
    addr = CTH_PTN.matcher(addr).replaceAll("COUNTY ROAD");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern USH_PTN = Pattern.compile("\\bUSH\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern CTH_PTN = Pattern.compile("\\bCTH\\b", Pattern.CASE_INSENSITIVE);
  
}
