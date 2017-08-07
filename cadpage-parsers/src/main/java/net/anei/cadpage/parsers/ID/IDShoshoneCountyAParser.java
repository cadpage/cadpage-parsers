package net.anei.cadpage.parsers.ID;


import net.anei.cadpage.parsers.dispatch.DispatchA16Parser;
/**
 * Shoshone county, CT
 */
public class IDShoshoneCountyAParser extends DispatchA16Parser {
  
  public IDShoshoneCountyAParser() {
    super(CITY_LIST, "SHOSHONE COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "administrator@shoshoneso.com,internal@shoshoneso.com";
  }
  
  private static final String[] CITY_LIST= new String[]{
    
    //  Cities
    "KELLOGG",
    "MULLAN",
    "OSBURN",
    "PINEHURST",
    "SMELTERVILLE",
    "WALLACE",
    "WARDNER",
    
    // Unincorporated communities
    "AVERY",
    "BIG CREEK",
    "BURKE",
    "CATALDO",
    "CLARKIA",
    "ENAVILLE",
    "MURRAY",
    "PINE CREEK",
    "KINGSTON",
    "SILVERTON"
  };
}
