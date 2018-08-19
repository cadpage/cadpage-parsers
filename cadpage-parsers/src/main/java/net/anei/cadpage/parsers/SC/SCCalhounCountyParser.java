package net.anei.cadpage.parsers.SC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class SCCalhounCountyParser extends DispatchA48Parser {
  
  public SCCalhounCountyParser() {
    super(CITY_LIST, "CALHOUN COUNTY", "SC",FieldType.NONE, A48_ONE_WORD_CODE, 
        Pattern.compile("\\d{1,3}-[A-Z]+"));
        setupCallList(CALL_LIST);
  }
  
  @Override
  public String getFilter() {
    return "CAD@calhouncounty.gov";
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
    
      "ALARM",
      "ALTERED MENTAL STATUS",
      "ASSIST OTHER AGENCY",
      "CARDIC ARREST",
      "GAS LEAK/PROPANE",
      "GUNSHOT WOUND",
      "ROAD BLOCKAGE",
      "STRUCTURE FIRE",
      "SUSPICIOUS ACTIVITY",
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
