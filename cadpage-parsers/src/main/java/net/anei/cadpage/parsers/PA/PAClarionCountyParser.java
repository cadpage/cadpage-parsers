package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/*
Clarion County, PA
 */


public class PAClarionCountyParser extends GroupBestParser {
  
  public PAClarionCountyParser() {
    super(new PAClarionCountyAParser(), new PAClarionCountyBParser(), 
          new PAClarionCountyCParser(), new PAClarionCountyDParser());
  }
  

  static void fixCity(Data data) {
    if (data.strCity.toUpperCase().endsWith(" BORO")) {
      data.strCity = data.strCity.substring(0,data.strCity.length()-5).trim();
    }
  }

  static final String[] CITY_LIST = new String[]{
    "ASHLAND",
    "BUTLER",
    "CLARION",
    "PARKER",
    "PARKER CITY",
    "RICHLAND",
    
    "BARKEYVILLE BORO",
    "BRUIN BORO",
    "CALLENSBURG BORO",
    "CLARION BORO",
    "EAST BRADY BORO",
    "EMLENTON BORO",
    "FOXBURG BORO",
    "HAWTHORN BORO",
    "KNOX BORO",
    "NEW BETHLEHEM BORO",
    "RIMERSBURG BORO",
    "SHIPPENVILLE BORO",
    "SLIGO BORO",
    "SAINT PETERSBURG BORO",
    "ST PETERSBURG BORO",
    "STRATTANVILLE BORO",
    
    "ALLEGENY TWP",
    "ASHLAND TWP",
    "BEAVER TWP",
    "BRADY TWP",
    "CLARION TWP",
    "ELK TWP",
    "FARMINGTON TWP",
    "HIGHLAND TWP",
    "HOVEY TWP",
    "KNOX TWP",
    "LICKING TWP",
    "LIMESTONE TWP",
    "MADISON TWP",
    "MILLCREEK TWP",
    "MONROE TWP",
    "PAINT TWP",
    "PARKER TWP",
    "PERRY TWP",
    "PINEY TWP",
    "PORTER TWP",
    "REDBANK TWP",
    "RICHLAND TWP",
    "SALEM TWP",
    "TOBY TWP",
    "WASHINGTON TWP"
  };

}
