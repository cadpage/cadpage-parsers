package net.anei.cadpage.parsers.SC;
import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class SCAbbevilleCountyAParser extends DispatchB2Parser {
 
  public SCAbbevilleCountyAParser() {
    super("stat12:", CITY_LIST, "ABBEVILLE COUNTY", "SC");
    setupCallList(CALL_LIST);
  }
  
  @Override
  public String getFilter() {
    return "E911@abbeville911.com";
  }
  
  private static CodeSet CALL_LIST = new CodeSet(
      "52C-CARDIAC ARREST",
      "50PI-TRAFFIC ACCIDENT W/INJURY",
      "TREE DOWN"
  );
  
  private static final String[] CITY_LIST = new String[]{

    // City
    "ABBEVILLE",

    // Towns
    "CALHOUN FALLS",
    "DONALDS",
    "DUE WEST",
    "HONEA PATH",
    "LOWNDESVILLE",
    "WARE SHOALS",

    // Unincorporated communities
    "ANTREVILLE",
    "LAKE SECESSION"
  };
}
