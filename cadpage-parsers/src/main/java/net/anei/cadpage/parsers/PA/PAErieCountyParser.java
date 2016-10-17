package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

public class PAErieCountyParser extends GroupBestParser {
  
  public PAErieCountyParser() {
    super(new PAErieCountyAParser(), 
          new PAErieCountyBParser(), 
          new PAErieCountyCParser(),
          new PAErieCountyEmergyCareParser());
  }
  
  
  public static final String[] CITY_LIST = new String[]{
    "CORRY",
    "ERIE",
    
    "ALBION",
    "CRANESVILLE",
    "EDINBORO",
    "ELGIN",
    "FAIRVIEW",
    "GIRARD",
    "LAKE CITY",
    "MCKEAN",
    "MILL VILLAGE",
    "NORTH EAST",
    "PLATEA",
    "UNION CITY",
    "WATERFORD",
    "WATTSBURG",
    "WESLEYVILLE",
    
    "AMITY TWP",
    "CONCORD TWP",
    "CONNEAUT TWP",
    "ELK CREEK TWP",
    "FAIRVIEW TWP",
    "FRANKLIN TWP",
    "GIRARD TWP",
    "GREENE TWP",
    "GREENFIELD TWP",
    "HARBORCREEK TWP",
    "LAWRENCE PARK TWP",
    "LEBOEUF TWP",
    "MCKEAN TWP",
    "MILLCREEK TWP",
    "NORTH EAST TWP",
    "SPRINGFIELD TWP",
    "SUMMIT TWP",
    "UNION TWP",
    "VENANGO TWP",
    "WASHINGTON TWP",
    "WATERFORD TWP",
    "WAYNE TWP",
    
    // Crawford County
    "CRAWFORD",
    "CRAWFORD COUNTY"
  };

}