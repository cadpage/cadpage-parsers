package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA41Parser;

public class INPorterCountyParser extends DispatchA41Parser {

  private static final Pattern MUT_AID_CALL_PTN = Pattern.compile("MUT.AID.*?(?: ([A-Z]{3}))?");

  public INPorterCountyParser() {
    super(CITY_CODES, "PORTER COUNTY", "IN", "[A-Z]{2}");
  }
  
  @Override
  public String getFilter() {
    return "pcdisp@porterco-ps.org,@pc911.porterco.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_ADD_DEFAULT_CNTY; 
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!parseMsg(body, data)) return false;
    
    // Winfield TWP is in Lake county
    if (data.strCity.equals("Crown Point") ||
        data.strCity.equals("Winfield Twp")) data.defCity = "LAKE COUNTY";
    else if (data.strCity.equals("OOC")) {
      data.defCity = "";
      data.strCity = "";
    }
    
    // If we don't have a city specified, and this is a mutual aid call
    // Change the default county to match the destination department
    if (data.strCity.length() == 0) {
      Matcher match = MUT_AID_CALL_PTN.matcher(data.strCall);
      if (match.matches()) {
        data.defCity = "";
        String dest = match.group(1);
        if (dest != null) {
          if (dest.equals("SCN")) data.defCity = "LAKE COUNTY";
          else if (dest.equals("OWN")) data.defCity = "LAKE COUNTY";
        }
      }
    }
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return EST_PTN.matcher(addr).replaceAll("ESTATES");
  }
  private static final Pattern EST_PTN = Pattern.compile("\\bEST\\b", Pattern.CASE_INSENSITIVE);
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BHB", "Burns Harbor",
      "BSH", "Beverly Shores",
      "CHE", "Chesterton",
      "DAC", "Dune Acres",
      "HEB", "Hebron",
      "KTS", "Kouts",
      "OGD", "Ogden Dunes",
      "PTG", "Portage",
      "PTR", "Porter",
      "VAL", "Valparaiso",
              
      "BNT", "Boone Twp",
      "CCT", "Center Twp",
      "CTT", "Center Twp",
      "ECT", "Eagle Creek Twp",
      "JKT", "Jackson Twp",
      "LBT", "Liberty Twp",
      "MGT", "Morgan Twp",
      "PGT", "Portage Twp",
      "PLT", "Pleasant Twp",
      "PNT", "Pine Twp",
      "POT", "Porter Twp",
      "UNT", "Union Twp",
      "WCT", "Westchester Twp",
      "WGT", "Washington Twp",
      
      // Lake County
      "CPT", "Crown Point",
      "WNT", "Winfield Twp",
      
      // OUT OF COUNTY
      "OOC",      "OOC"
  });
}
