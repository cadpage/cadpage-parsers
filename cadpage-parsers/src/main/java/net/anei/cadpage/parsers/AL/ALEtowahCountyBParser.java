package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class ALEtowahCountyBParser extends DispatchA65Parser {
  
  public ALEtowahCountyBParser() {
    super(CITY_LIST, "ETOWAH COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911comm2.info,dispatch@etowah911.info";
  }
  
  private static final String[] CITY_LIST = new String[]{
    
//Cities
      
      "ATTALLA",
      "BOAZ",
      "GADSDEN",
      "GLENCOE",
      "HOKES BLUFF",
      "RAINBOW CITY",
      "SOUTHSIDE",

  //Towns

      "ALTOONA",
      "REECE CITY",
      "RIDGEVILLE",
      "SARDIS CITY",
      "WALNUT GROVE",

  //Census-designated places

      "BALLPLAY",
      "BRISTOW COVE",
      "CARLISLE-ROCKLEDGE",
      "COATS BEND",
      "EGYPT",
      "GALLANT",
      "IVALEE",
      "LOOKOUT MOUNTAIN",
      "NEW UNION",
      "TIDMORE BEND",
      "WHITESBORO",

  //Unincorporated communities

      "ANDERSON",
      "LIBERTY HILL",
      "MOUNTAINBORO",
      "BOAZ",
      "PILGRIMS REST",
      "BAIRDVILLE",

  };
}
