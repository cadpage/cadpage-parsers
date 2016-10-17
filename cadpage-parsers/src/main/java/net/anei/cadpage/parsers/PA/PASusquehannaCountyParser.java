package net.anei.cadpage.parsers.PA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class PASusquehannaCountyParser extends DispatchB2Parser {

  public PASusquehannaCountyParser() {
    super(CITY_LIST, "SUSQUEHANNA COUNTY", "PA");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "@susqco.alertpa.org,no-reply@ecnalert.com,777";
  }
  
  private boolean good;
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    good = subject.equals("Susq Co CAD") || subject.equals("Alert Message");
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!super.parseMsg(body, data)) return false;
    data.strCity = data.strCity.toUpperCase();
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    data.strCity = stripFieldEnd(data.strCity, " BOROUGH");
    if (NY_CITY_SET.contains(data.strCity)) data.strState = "NY";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  protected boolean isPageMsg(String body) {
    if (good) return true;
    return super.isPageMsg(body);
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BARNES MILL",
    "BETHEL HILL",
    "BUNNELL FARM",
    "COXTON LAKE",
    "ELK MOUNTAIN",
    "FIDDLE LAKE",
    "FISK MILL",
    "FOREST LAKE",
    "GAGE QUARRY",
    "GREEN VALLEY",
    "LAKE SHORE",
    "LAUREL LAKE",
    "LEWIS LAKE",
    "LITTLE IRELAND",
    "MESHOPPEN CREEK",
    "MOUNTAIN VALLEY",
    "MUCKEY RUN",
    "ORPHAN SCHOOL",
    "PARKER HILL",
    "PENNSWORTH HILL",
    "POST POND",
    "SCHOOL HOUSE",
    "SHELDON HILL"
      
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANGUP",
      "ABDOMINAL PAIN",
      "ALLERGIES",
      "ANIMAL BITES/ATTACKS",
      "BACK PAIN (NON TRAUMA)",
      "BREATHING PROBLEMS",
      "BRUSH FIRE",
      "CARB MONOX/INHALATION/HAZMAT",
      "CARD/RESP ARREST/DEATH",
      "CHEST PAIN",
      "CONVULSIONS/SEIZURES",
      "DIABETIC PROBLEMS",
      "ELECTRICAL POLE ON FIRE",
      "FALLS",
      "HAZARDOUS MATERIAL",
      "HEART PROBLEMS/AICD",
      "HEMORRHAGE/LACERATIONS",
      "INACCESSIBLE/OTHER ENTRAPMENTS",
      "MED CALL",
      "MISCELLANEOUS",
      "MOTOR VEHICLE ACCIDENT",
      "OVERDOSE/POISONING",
      "PSYCH/BEHAVE/SUICIDE ATTEMPT",
      "PUMPING DETAIL",
      "SEARCH AND RESCUE",
      "SICK PERSON",
      "SMOKE INVESTIGATION",
      "STAB/GSW/PENETRATING TRAUMA",
      "STAND BY",
      "STROKE/CVA",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE ALARM",
      "STRUCTURE FIRE CHIMNEY",
      "SUSPICIOUS PERSON",
      "TEST CALL",
      "TRAUMATIC INJURIES (SPECIFIC)",
      "TREES DOWN",
      "UNCONSCIOUS/FAINTING",
      "UNKNOWN PROBLEM/MAN DOWN",
      "UNKNOWN FIRE",
      "WIRES DOWN"
  );

  private static final String[] CITY_LIST = new String[]{
    "FOREST CITY",
    "FOREST CITY BORO",
    "FRIENDSVILLE",
    "FRIENDSVILLE BORO",
    "GREAT BEND",
    "GREAD BEND BORO",
    "HALLSTEAD",
    "HALLSTEAD BORO",
    "HOP BOTTOM",
    "HOP BOTTOM BORO",
    "LANESBORO",
    "LANESBORO BORO",
    "LITTLE MEADOWS",
    "LITTLE MEADOWS BORO",
    "MONTROSE",
    "MONTROSE BORO",
    "NEW MILFORD",
    "NEW MILFORD BORO",
    "OAKLAND",
    "OAKLAND BORO",
    "SUSQUEHANNA DEPOT",
    "SUSQUEHANNA DEPOT BORO",
    "THOMPSON",
    "THOMPSON BORO",
    "UNION DALE",
    "UNION DALE BORO",
    "UNIONDALE",
    "UNIONDALE BORO",
    
    "APOLACON",
    "APOLACON TWP",
    "ARARAT",
    "ARARAT TWP",
    "AUBURN",
    "AUBURN TWP",
    "BRIDGEWATER",
    "BRIDGEWATER TWP",
    "BROOKLYN",
    "BROOKLYN TWP",
    "CHOCONUT",
    "CHOCONUT TWP",
    "CLIFFORD",
    "CLIFFORD TWP",
    "DIMOCK",
    "DIMOCK TWP",
    "FOREST LAKE",
    "FOREST LAKE TWP",
    "FRANKLIN",
    "FRANKLIN TWP",
    "GIBSON",
    "GIBSON TWP",
    "GREAT BEND",
    "GREAT BEND TWP",
    "HARFORD",
    "HARFORD TWP",
    "HARMONY",
    "HARMONY TWP",
    "HERRICK",
    "HERRICK TWP",
    "JACKSON",
    "JACKSON TWP",
    "JESSUP",
    "JESSUP TWP",
    "LATHROP",
    "LATHROP TWP",
    "LENOX",
    "LENOX TWP",
    "LIBERTY",
    "LIBERTY TWP",
    "MIDDLETOWN",
    "MIDDLETOWN TWP",
    "NEW MILFORD",
    "NEW MILFORD TWP",
    "OAKLAND",
    "OAKLAND TWP",
    "RUSH",
    "RUSH TWP",
    "SILVER LAKE",
    "SILVER LAKE TWP",
    "SPRINGVILLE",
    "SPRINGVILLE TWP",
    "THOMPSON",
    "THOMPSON TWP",
    
    // Lackawanna County
    "GREENFIELD",
    "GREENFIELD TWP",
    "VANDLING BORO",
    "VANDLING",
    
    // Broome County, NY
    "WINDSOR",
    
    // Wayne County
    "MT PLEASANT",
    "MT PLEASANT TWP",
    "STARRUCCA BOR",
    "STARRUCCA BOROUGH"
  };
  
  private static final Set<String> NY_CITY_SET = new HashSet<String>(Arrays.asList(new String[]{
    "WINDSOR"
  }));
}
