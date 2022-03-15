package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class OHPickawayCountyAParser extends DispatchEmergitechParser {
  
  public OHPickawayCountyAParser() {
    super(60, CITY_LIST, "PICKAWAY COUNTY", "OH");
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }
  
  @Override
  public String getFilter() {
    return "911@pickawaysheriff.com";
  }

  private static final String[] CITY_LIST = new String[]{
    
    // City
    "CIRCLEVILLE",

    // Villages
    "ASHVILLE",
    "COMMERCIAL POINT",
    "DARBYVILLE",
    "HARRISBURG",
    "LOCKBOURNE",
    "NEW HOLLAND",
    "ORIENT",
    "SOUTH BLOOMFIELD",
    "TARLTON",
    "WILLIAMSPORT",
    "WEST LOCKBOURNE",

    // Townships
    "CIRCLEVILLE TWP",
    "DARBY TWP",
    "DEER CREEK TWP",
    "HARRISON TWP",
    "JACKSON TWP",
    "MADISON TWP",
    "MONROE TWP",
    "MUHLENBERG TWP",
    "PERRY TWP",
    "PICKAWAY TWP",
    "SALT CREEK TWP",
    "SCIOTO TWP",
    "WALNUT TWP",
    "WASHINGTON TWP",
    "WAYNE TWP",

    // Census-designated places
    "DERBY",
    "LOGAN ELM VILLAGE",

    // Unincorporated communities
    "ATLANTA",
    "CROWNOVER MILL",
    "DUVALL",
    "EAST RINGGOLD",
    "ELMWOOD",
    "ERA",
    "FIVE POINTS",
    "FOX",
    "GRANGE HALL",
    "HAYESVILLE",
    "KINDERHOOK",
    "LEISTVILLE",
    "LITTLE WALNUT",
    "MATVILLE",
    "MEADE",
    "MILLPORT",
    "PHERSON",
    "ROBTOWN",
    "SAINT PAUL",
    "STRINGTOWN",
    "THACHER",
    "WALNUT",
    "WESTFALL",
    "WHISLER",
    "WOODLYN"
  };

}
