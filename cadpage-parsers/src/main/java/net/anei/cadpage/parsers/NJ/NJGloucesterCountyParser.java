package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Gloucester County, NJ
*/


public class NJGloucesterCountyParser extends GroupBestParser {
  
  public NJGloucesterCountyParser() {
    super(new NJGloucesterCountyAParser(), new NJGloucesterCountyBParser());
  }

  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "A", "CLAYTON",
      "B", "DEPTFORD",
      "C", "EAST GREENWICH TWP",
      "CF","WEST DEPFORD TWP",
      "D", "ELK TWP",
      "E", "FRANKLIN TWP",
      "F", "GLASSBORO",
      "G", "GIBBSTOWN",
      "H", "HARRISON TWP",
      "I", "LOGAN TWP",
      "J", "MANTUA TWP",
      "K", "MONROE TWP",
      "L", "NATIONAL PARK",
      "M", "NEWFIELD",
      "N", "PAULSBORO",
      "O", "PITMAN",
      "P", "SOUTH HARRISON TWP",
      "Q", "SWEDESBORO",
      "R", "WASHINGTON TWP",
      "S", "WENONAH",
      "T", "WEST DEPTFORD TWP",
      "Q", "SWEDESBORO",
      "U", "WESTVILLE BORO",
      "V", "WOODBURY CITY",
      "W", "WOODBURY HEIGHTS",
      "X", "WOOLWICH TWP",
      "Y", "ROWAN",
      "04", "BUENA",
      "05", "BUENA VISTA TWP",
      "07", "EGGHARBOR CITY",
      "09", "ESTELL MANOR",
      "10", "FOLSOM",
      "23", "WEYMOUTH",
      "CU", "CUMBERLAND COUNTY",
      "SA", "SALEM COUNTY"
  });
      
// Unidentified      
//      AC = MAYS LANDING?
//      PX = PA??     

}
