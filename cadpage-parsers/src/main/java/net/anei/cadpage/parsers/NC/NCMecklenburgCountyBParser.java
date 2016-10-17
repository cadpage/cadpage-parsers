package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Mecklenburg County, NC (variant B)
 */
public class NCMecklenburgCountyBParser extends SmartAddressParser {
  
  private static final Pattern CODE_PTN = Pattern.compile("(?<! ) (\\d{2}[A-Z]{0,2})-(?!\\d)");
  private static final Pattern UNIT_PTN = Pattern.compile("\\b[A-Z][A-Za-z]{1,4}\\d{1,3}\\b");
  
  
  public NCMecklenburgCountyBParser() {
    super("MECKLENBURG COUNTY", "NC");
    setFieldList("ADDR APT INFO PRI CODE CALL X UNIT MAP ID");
    setupMultiWordStreets(
        "60 FOOT"
    );
  }
  
  @Override
  public String getFilter() {
    return "@minthillvfd.com,@huntersvillefd.com,alerts@cadpage.org,cadmail@medic911.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {

    Matcher match = CODE_PTN.matcher(body);
    if (!match.find()) return false;
    
    data.strCode = match.group(1);
    String part1 = body.substring(0,match.start()).trim();
    String part2 = body.substring(match.end()).trim();
    
    Parser p = new Parser(part2);
    part2 = p.get("Map -");
    data.strMap = p.get(' ');
    data.strCallId = p.get(' ');
    
    match = UNIT_PTN.matcher(part2);
    if (!match.find()) return false;
    data.strUnit = part2.substring(match.start()).trim();
    part2 = part2.substring(0,match.start()).trim();
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ONLY_CROSS | FLAG_ANCHOR_END, part2, data);
    
    int pt = part1.lastIndexOf(' ');
    if (pt >= 0) {
      data.strPriority = part1.substring(pt+1, pt+2);
      part1 = part1.substring(0,pt).trim();
    }
    parseAddress(StartType.START_ADDR, part1, data);
    String sInfo = getLeft();
    if (sInfo.endsWith("-")) sInfo = sInfo.substring(0,sInfo.length()-1).trim();
    data.strSupp = sInfo;
    
    
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return PK_PTN.matcher(addr).replaceAll("PKWY");
  }
  private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b", Pattern.CASE_INSENSITIVE);

}
