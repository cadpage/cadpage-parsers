package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class OHFultonCountyParser extends DispatchEmergitechParser {
  
  public OHFultonCountyParser() {
    super(CITY_LIST, "FULTON COUNTY", "OH", TrailAddrType.PLACE_INFO);
    removeWords("TRAIL");
  }
 
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    body = subject + ':' + body + "$$";
    return super.parseMsg(body, data);
  }
  

  @Override
  protected boolean parseFields(String[] fields, Data data) {
    fields[fields.length-1] = stripFieldEnd(fields[fields.length-1], "$$");
    return super.parseFields(fields, data);
  }


  private static final String[] CITY_LIST = new String[]{
    
    "FULTON CO",
    
    //CITY
    "WAUSEON",
    
    //VILLAGES
    "ARCHBOLD",
    "DELTA",
    "FAYETTE",
    "LYONS",
    "METAMORA",
    "SWANTON",

    //TOWNSHIPS
    "AMBOY TWP",
    "CHESTERFIELD TWP",
    "CLINTON TWP",
    "DOVER TWP",
    "FRANKLIN TWP",
    "FULTON TWP",
    "GERMAN TWP",
    "GORHAM TWP",
    "PIKE TWP",
    "ROYALTON TWP",
    "SWAN CREEK TWP",
    "YORK TWP",

   //OTHER
    "PETTISVILLE",
    "TEDROW",
    
    // Lucas County
    "MAUMEE",
    
    // Williams County
    "ALVORDTON",
    "MILL CREEK TWP"
  };
}
