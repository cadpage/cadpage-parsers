package net.anei.cadpage.parsers.GA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class GALibertyCountyParser extends DispatchB2Parser {

  public GALibertyCountyParser() {
    super("911COMMUNICATIONS:", CITY_LIST,"LIBERTY COUNTY", "GA", B2_FORCE_CALL_CODE);
    setupMultiWordStreets(
        "BARRY MCCAFFREY",
        "BILL CARTER",
        "CHARLIE BUTLER",
        "CHERRIE MURRELL",
        "COUNTY LINE",
        "CROSS CREEK",
        "DUTCHMANS COVE",
        "EB COOPER",
        "EG MILES",
        "GENERAL SCREVEN",
        "GENERAL STEWART",
        "GLENN BRYANT",
        "FORT MORRIS",
        "INTERSTATE PAPER",
        "JOHN WELLS",
        "LEROY COFFER",
        "LEWIS FRASIER",
        "LIVE OAK CHURCH",
        "LONG FRASIER",
        "MAGNOLIA PLANTATION",
        "MILES CROSSING",
        "OAK CREST",
        "PATE ROGERS",
        "RYE PATCH",
        "SEABROOK ISLAND",
        "SEABROOK SCHOOL",
        "SHADOW WALK",
        "WEEPING WILLOW",
        "WELLS CEMETERY"
    );
    addRoadSuffixTerms("HIGHWAY");
    removeWords("PLACE");
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = EG_MILES_PTN.matcher(addr).replaceAll("ELMA G MILES");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern EG_MILES_PTN = Pattern.compile("\\bE ?G MILES\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("FAIRHAVEN ACRES TP")) city = "MIDWAY";
    return super.adjustMapCity(city);
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ALLENHURST",
    "FLEMINGTON",
    "FORT STEWART",
    "GUMBRANCH",
    "HINESVILLE",
    "MIDWAY",
    "SUNBURY",
    "RICEBORO",
    "WALTHOURVILLE",
    
    "FAIRHAVEN ACRES TP"
  };
  
  @Override
  protected CodeSet buildCallList() {
    return new CodeSet(
        "ABDOMINAL/BACK PAIN",
        "ANIMAL BITE",
        "ANIMAL CALL",
        "APPLIANCE FIRE",
        "ASSIST CITIZEN",
        "BRUSH FIRE",
        "BROKEN BONE",
        "CHEST PAIN/CARDIAC SYMPTOMS",
        "DIABETIC EMERGENCY",
        "DISTURBANCE",
        "DOMESTIC DISPUTE IN PROGRESS",
        "DOMESTIC RESCUE",
        "DOWNED WIRES",
        "DWELLING FIRE(SFH)",
        "EMS MISCELLANEOUS",
        "FAILURE TO STOP",
        "FALL VICTIM",
        "FIRE TEST CALL",
        "GAS LEAK/OUTSIDE",
        "HAZMAT INCIDENT",
        "HEADACHE/STROKE",
        "HEMORRHAGE/BLEEDING",
        "INJURY OTHER THAN FALL",
        "JUVENILE DELIQUENCY",
        "LABOR/EMINENT BIRTH",
        "LACERATION",
        "MEDICAL ALARM",
        "MESSAGE DELIVERY",
        "MISSING PERSON",
        "PSYCHIATRIC EMERGENCY",
        "RESPIRATORY DISTRESS",
        "SICK/UNKNOWN ILLNESS",
        "SEIZURE",
        "SEXUAL ASSAULT/RAPE",
        "SUICIDE THREAT",
        "STORM DAMAGE",
        "SUICIDE ATTEMPT",
        "SYNCOPAL EPISODE",
        "TRAFFIC ACCIDENT NO INJURY",
        "TRAFFIC ACCIDENT WITH INJURIES",
        "TRAFFIC STOP (CLI)",
        "TRANSPORT",
        "TRUCK FIRE",
        "UNCONSCIOUS",
        "UNKNOWN PROBLEM",
        "UNRESPONSIVE",
        "VEHICLE FIRE",
        "VEHICLE THEFT",
        "VIN VERIFICATION"

    );
  }
  
}
