package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class GAJacksonCountyAParser extends DispatchB2Parser {

  public GAJacksonCountyAParser() {
    super("JACKSONE911:||JACKSON CO SO JACKSONE911:||JACKSON CO SO: JACKSONE911:", CITY_LIST, "JACKSON COUNTY", "GA", B2_FORCE_CALL_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "APPLE VALLEY",
        "AT TOM WHITE",
        "BAKER VIEW",
        "BAKERS FARM",
        "BILL WATKINS",
        "BILL WRIGHT",
        "BRADLEY SPRINGS",
        "BRASELTON INDUSTRIAL",
        "CHARLIE SMITH",
        "COOPER BRIDGE",
        "CURK ROBERTS",
        "DEER CREEK",
        "G BARNETT",
        "G LEGG",
        "HOYT HOLDER",
        "HOYT WOOD",
        "INDIAN CREEK",
        "JACKSON COMMONS",
        "JACKSON TRAIL",
        "JESSE CRONIC",
        "LAGREE DUCK",
        "LAMAR COOPER",
        "LEWIS BRASELTON",
        "LIBERTY CHURCH",
        "MEADOW VISTA",
        "OLDE WICK",
        "POSSUM CREEK",
        "RAFORD WILSON",
        "REMINGTON PARK",
        "ROYAL CREST",
        "SUMMER SPRINGS",
        "THOMPSON MILL",
        "TOM WHITE",
        "TOWNE CTR",
        "TURNING LEAF",
        "WALNUT RIDGE",
        "WALNUT WOODS",
        "WEST JACKSON COMMONS",
        "WEST JACKSON",
        "WOOD CREST",
        "ZION CHURCH"
   );
    addRoadSuffixTerms("CRK");
  }
  
  @Override
  public String getFilter() {
    return "93001,777,14101,2002,JACKSONE911@jacksoncountygov.com";
  }
  
  @Override
  protected boolean parseAddrField(String field, Data data) {
    field = field.replace(" AT ", " & ").replace('@', '&');
    return super.parseAddrField(field, data);
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "1070 FIRE",
      "1070R RESIDENTIAL FIRE",
      "ABDOMINAL PAIN",
      "ACCIDENT - INJURIES",
      "ACCIDENT - NO INJURIES",
      "ACTIVITY",
      "ALARM-ADVISE TYPE",
      "ALLERGIC REACTION",
      "ASSIST",
      "ASSISTING MOTORIST",
      "BOLO",
      "CANCER PATIENT",
      "CARDIAC ARREST",
      "CHEST PAIN/RESPIRATORY DISTRES",
      "COMMERCIAL FIRE",
      "CONVULSION/SEIZURE",
      "DIABETIC PATIENT",
      "DRUG OVERDOSE",
      "FIRE",
      "FRACTURE",
      "HEMORRHAGE",
      "HIT AND RUN",
      "ILLEGAL",
      "INJURY",
      "KEYS LOCKED IN VEH W/OCCP",
      "MEDICAL ALERT ACTIVATION",
      "MISSING PERSON OR JUVENILE",
      "NOT REGULAR CALL",
      "OB CALL",
      "PROBLEM UNKNOWN",
      "RESIDENTIAL FIRE",
      "SIG 12",
      "SMOKE INVESTIGATION",
      "STOPPING SUSPICIOUS VEHICLE",
      "STROKE/CVA PATIENT",
      "SUBJECT ILL",
      "SUICIDE OR ATTEMPT",
      "SYNCOPAL EPISODE (FAINTING)",
      "TRANSPORT PATIENT"
  );
  
  private static final String[] CITY_LIST = new String[]{
    
    // cities and towns
    "ARCADE",
    "BRASELTON",
    "COMMERCE",
    "HOSCHTON",
    "JEFFERSON",
    "MAYSVILLE",
    "NICHOLSON",
    "PENDERGRASS",
    "TALMO",
    
    // unincorporated communities
    "APPLE VALLEY",
    "ATTICA",
    "BROCKTON",
    "CENTER",
    "CLARKSBORO",
    "CONSTANTINE",
    "DRY POND",
    "EDNAVILLE",
    "FAIRVIEW",
    "GROVE LEVEL",
    "HOLDERS",
    "HOLLY SPRINGS",
    "RED STONE",
    "SELLS",
    "STONEHAM",
    "THOMPSONS MILLS",
    "THURMACK",
    "THYATIRA",
    "WILSONS CHURCH"

  };
}
