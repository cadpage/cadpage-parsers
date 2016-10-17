package net.anei.cadpage.parsers.MN;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MNAnokaCountyAParser extends SmartAddressParser {
  
  public MNAnokaCountyAParser() {
    super("ANOKA COUNTY", "MN");
    setFieldList("SRC CALL ADDR APT PLACE INFO ID");
  }
  
  @Override
  public String getFilter() {
    return "cad.cad@co.anoka.mn.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Extract primary call description
    int pt = body.indexOf("CAD MSG: *D ");
    if (pt < 0) return false;
    body = body.substring(pt);
    
    if (body.length() < 26) return false;
    Parser p = new Parser(body.substring(12, 26).trim());
    data.strSource = p.get(' ');
    data.strCall = p.get();
    String sDesc = CALL_CODES.getProperty(data.strCall);
    if (sDesc != null) data.strCall = data.strCall + " - " + sDesc;
    
    body = body.substring(26);
    
    // Extract call ID if there is one
    pt = body.lastIndexOf(" INC:");
    if (pt >= 0) {
      int pt2 = body.indexOf(' ', pt+5);
      if (pt2 < 0) pt2 = body.length();
      data.strCallId = body.substring(pt+5, pt2);
      body = body.substring(0, pt).trim();
    }
    
    // Normally a double blank separate the place & address line from the supp information
    
    pt = body.indexOf("  ");
    if (pt >= 0) {
      String addressLine = body.substring(0, pt);
      data.strSupp = body.substring(pt+2).trim();
      
      // An @ separates the place name from address,
      // But we cannot assume which one comes first. we have to check both
      // sides to see which makes a better address.
      
      // Which one we pick has to be parsed a second time to pick up some
      // of the subtle changes the parser makes
      pt = addressLine.indexOf('@');
      if (pt < 0) {
        parseAddress(StartType.START_ADDR, FLAG_ALLOW_DUAL_DIRECTIONS, addressLine, data);
      } else {
        String part1 = addressLine.substring(0, pt).trim();
        String part2 = addressLine.substring(pt+1).trim();
        Result res1 = parseAddress(StartType.START_ADDR, FLAG_ALLOW_DUAL_DIRECTIONS | FLAG_CHECK_STATUS | FLAG_ANCHOR_END, part1);
        Result res2 = parseAddress(StartType.START_ADDR, FLAG_ALLOW_DUAL_DIRECTIONS | FLAG_CHECK_STATUS | FLAG_ANCHOR_END, part2);
        if (res1.getStatus() > res2.getStatus()) {
          part1 = part2;
          res2 = res1;
        }
        data.strPlace = part1;
        res2.getData(data);
      }
    }
    
    // If we don't find the expected double blank, parser it as a name/address
    // field followed by supplemental info
    
    else {
      body = body.replaceAll("@", "");
      parseAddress(StartType.START_PLACE, body, data);
      data.strSupp = getLeft();
    }
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = COUNTY_X_PKWY_PTN.matcher(addr).replaceAll("COUNTY ROAD $1");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern COUNTY_X_PKWY_PTN = Pattern.compile("\\bCOUNTY ([A-Z]) PKWY\\b", Pattern.CASE_INSENSITIVE);
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "27F",  "Bomb Threat",
      "32M",  "Medical",
      "32R",  "Rescue",
      "32X",  "PI Accident",
      "39F",  "Alarm (Fire)",
      "39CO", "Alarm (Carbon Monoxide)",
      "45F",  "Fire Drill",
      "50",   "Fire (Unknown)",
      "51",   "Fire (Building)",
      "52",   "Fire (Grass)",
      "53",   "Fire (Vehicle)",
      "54",   "Fire (Refuse)",
      "55",   "Fire (Electrical)",
      "56",   "Smoke/Gas Odor",
      "57",   "Fire (Misc)",
      "59",   "Police Assist",
      "PC59", "Phone Call",
      "73F",  "Road Closure Info"
  });
}
