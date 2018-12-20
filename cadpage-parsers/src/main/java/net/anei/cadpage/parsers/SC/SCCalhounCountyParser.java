package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class SCCalhounCountyParser extends DispatchA48Parser {
  
  public SCCalhounCountyParser() {
    super(CITY_LIST, "CALHOUN COUNTY", "SC",FieldType.NONE, A48_NO_CODE);
        setupCallList(CALL_LIST);
        setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "CAD@calhouncounty.gov";
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "A Z",
      "BROWNS CHAPEL",
      "BULL SWAMP",
      "COMMUNITY CLUB",
      "DOODLE HILL",
      "NUMBER SIX",
      "SANDY RUN",
      "STABLER HILL",
      "SUGAR HILL",
      "SWEET BAY",
      "THIRD BRANCH",
      "VALLEY RIDGE",
      "WHITE SANDS"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
    
      "ALARM",
      "ALTERED MENTAL STATUS",
      "ASSIST OTHER AGENCY",
      "CARDIC ARREST",
      "FRACTURE",
      "GAS LEAK/PROPANE",
      "GUNSHOT WOUND",
      "PAIN",
      "RESPIRATORY DISTRESS",
      "ROAD BLOCKAGE",
      "STRUCTURE FIRE",
      "SUSPICIOUS ACTIVITY",
      "UNCONSCIOUS",
      "VEHICLE ACCIDENT",
      "VEHICLE FIRE",
      "WOODS FIRE",
      "WORKING FIRE"
      
  );
  
  
  private static final String[] CITY_LIST = new String[]{
     
  //TOWNS    
      
      "CAMERON",
      "ELLOREE",
      "ST MATTHEWS",
      "SWANSEA",

  //UNINCORPORATED COMMUNITIES

      "CRESTON",
      "FORT MOTTE",
      "LONE STAR",
      "SANDY RUN"
    };
}
