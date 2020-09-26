package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MIRoscommonCountyParser extends DispatchOSSIParser {
  
  public MIRoscommonCountyParser() {
    super(CITY_CODES, "ROSCOMMON COUNTY", "MI", 
          "( CANCEL | ( FYI | EMPTY? ) CALL ) ADDR CITY? INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@roscommoncounty.net";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean noParseSubjectFollow() { return true; }
    };
  }
  
  private static final Pattern WIRELESS_REBID_PTN = Pattern.compile("\n\\[Wireless Re-bid\\] *");
  private static final Pattern WEIRD_MARK_PTN = Pattern.compile("\\[[^\\]\n]*?;|\n[A-Z]? *;");
  private static final Pattern TRAIL_ADDR_CITY_PTN = Pattern.compile("(?:\\[[^\\]\n]*?;|\n[A-Z]? *)?(;[^;\n]+;([A-Z]{3,4}))$");
  private static final Pattern TRAIL_ADDR_PTN = Pattern.compile("(?:\\[[^\\]\n]*?;|\n[A-Z]? *)?(;([^;\n]+))$");

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = WIRELESS_REBID_PTN.matcher(body).replaceAll("\n");

    // Fix weird trailing address/city mess
    String extra = null;
    Matcher match = WEIRD_MARK_PTN.matcher(body);
    if (match.find()) {
      extra = body.substring(match.end()-1);
      body = body.substring(0,match.start());
    } 
    else if ((match = TRAIL_ADDR_CITY_PTN.matcher(body)).find()) {
      String city = match.group(2);
      if (CITY_CODES.getProperty(city) != null) {
        extra = match.group(1);
        body = body.substring(0,match.start());
      }
    }
    else if ((match = TRAIL_ADDR_PTN.matcher(body)).find()) {
      String addr = match.group(2).trim();
      if (checkAddress(addr) > STATUS_STREET_NAME) {
        extra = match.group(1);
        body = body.substring(0,match.start());
      }
    }
    
    if (extra != null) {
      int pt = body.indexOf(';');
      if (pt >= 0) {
        if (body.substring(0,pt).endsWith(": ")) {
          pt = body.indexOf(';', pt+1);
        }
      }
      if (pt < 0) pt = body.length();
      body = body.substring(0,pt) + extra + body.substring(pt);
    }
    
    body = body.replace('\n', ';');
    return super.parseMsg(body, data);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AUS",  "AUSABLE TWP",
      "AUSA", "AUSABLE TWP",
      "BAC",  "BACKUS TWP",
      "BACK", "BACKUS TWP",
      "DEN",  "DENTON TWP",
      "DENT", "DENTON TWP",
      "GAP1", "GERRISH TWP",
      "GAP2", "GERRISH TWP",
      "GAP3", "GERRISH TWP",
      "GER",  "GERRISH TWP",
      "GERR", "GERRISH TWP",
      "HIG",  "HIGGINS TWP",
      "HIGG", "HIGGINS TWP",
      "LAK",  "LAKE TWP",
      "LAKE", "LAKE TWP",
      "LYO",  "LYON TWP",
      "LYON", "LYON TWP",
      "MAR",  "MARKEY TWP",
      "MARK", "MARKEY TWP",
      "NES",  "NESTER TWP",
      "NEST", "NESTER TWP",
      "RIC",  "RICHFIELD TWP",
      "RICH", "RICHFIELD TWP",
      "ROS",  "ROSCOMMON TWP",
      "ROSC", "ROSCOMMON TWP",
      "VILL", "ROSCOMMON"
  });
}
