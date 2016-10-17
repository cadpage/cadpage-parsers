package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;



public class KYCarrollCountyBParser extends DispatchB2Parser {
  
  public KYCarrollCountyBParser() {
    super("CARROLLCOUNTY911:", CITY_LIST, "CARROLL COUNTY", "KY");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  protected boolean parseAddrField(String field, Data data) {
    field = field.replace('@', '&');
    return super.parseAddrField(field, data);
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANGUP CALLS",
      "BURGLAR ALARM",
      "CALL AT SCHOOL",
      "CRIMINAL INVESTIGATION",
      "DOG COMPLAINT",
      "FIGHT IN PROGRESS",
      "INFORMATIONAL CALL",
      "MOTORIST ASSIST",
      "PROPERTY DAMAGE ACCIDENT",
      "RECKLESS DRIVING",
      "REQUEST AMBULANCE",
      "ROAD HAZARD",
      "TEST CALL",
      "THEFT REPORT",
      "TROUBLE CALL",
      "VISITOR PRESENT"
  ); 
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "COUNTY PARK",
    "HIGH SCHOOL",
    "REDWOOD HOMES",
    "SCHOOL 1706 HIGHLAND",
    "WHITES RUN"
   
  };
  
  private static final String[] CITY_LIST = new String[]{
      "CARROLLTON",
      "ENGLISH",
      "GHENT",
      "PRESTONVILLE",
      "SANDERS",
      "WORTHVILLE"
  };
}
