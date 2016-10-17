package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXLufkinParser extends DispatchA18Parser {
  
  public TXLufkinParser() {
    super(CITY_LIST, "ANGELINA COUNTY","TX");
  }
  
  @Override
  public String getFilter() {
    return "samhouston@cityoflufkin.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    "ANGELINA COUNTY",
    
    // Cities
    "BURKE",
    "DIBOLL",
    "HUDSON",
    "HUNTINGTON",
    "LUFKIN",
    "ZAVALLA",

    // Unincorporated areas
    "ALCO",
    "BALD HILL",
    "BEULAH",
    "CEDAR GROVE",
    "CENTRAL",
    "CLAWSON",
    "DAVISVILLE",
    "DOLAN",
    "DUNAGAN",
    "DURANT",
    "EWING",
    "HERTY",
    "HOMER",
    "JONESVILLE",
    "MARION",
    "MOFFITT",
    "OAK FLAT",
    "PEAVY",
    "PLATT",
    "POLLOK",
    "PRAIRIE GROVE",
    "REDLAND",
    "REDTOWN",
    "ROCKY SPRINGS",
    "SHADY GROVE",
    "SHAWNEE",
    "SHAWNEE PRAIRIE",
    "THOMAS CROSSING",
    "WOODLAWN"
  };
}
