package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;



public class KYFultonCountyParser extends DispatchB2Parser {
  
  public KYFultonCountyParser() {
    super("FULTON911:",CITY_LIST, "FULTON COUNTY", "KY", B2_FORCE_CALL_CODE);
    setupCallList(CALL_CODE);
    setupMultiWordStreets(
        "CHERRY LAUREL",
        "INDUSTRIAL PARK",
        "JOHN C JONES",
        "JORDAN FERGUSON",
        "JULIAN M CARROLL",
        "LEWIS WEAKS",
        "MYRON CORY",
        "PARK TERRACE",
        "STATE LINE",
        "STEPHEN BEALE",
        "TROY HICKMAN",
        "UNION CITY",
        "WOLF CREEK"
    );
  }
  
  @Override
  public String getFilter() {
    return "FULTON911@fultoncountysheriff.net";
  }

  private static final String[] CITY_LIST = new String[]{
    "FULTON",
    "HICKMAN"
    
  };
  
  private static final CodeSet CALL_CODE = new CodeSet(
      "ACCIDENT WITH INJURIES",
      "ALERT FCD ACTIVE 911 TESTING",
      "ANY KIND OF TESTING",
      "BLOOD PRESSURE PROBLEMS",
      "BREATHING PROBLEMS",
      "CHOKING",
      "CONVULSIONS/SEIZURES",
      "DEATH",
      "DISTURBANCE",
      "DIZZY LIGHTHEADED INCOHERENT",
      "DOMESTIC VIOLENCE",
      "EXTRA WATCH REQUEST",
      "FALLS",
      "FIGHT OR ASSAULT",
      "FIRE",
      "FIRE ALARM",
      "GENERAL SICKNESS",
      "HEART PROB/CHEST PAINS/ATTACK",
      "MEDICAL ALARM",
      "MURDER OR HOMICIDE",
      "POSSIBLE STROKE",
      "ROBBERY",
      "SEVERE WEATHER WARNING",    
      "STRUCTURE FIRE",
      "UNCONCIOUS PERSON"
        
   );   
}
