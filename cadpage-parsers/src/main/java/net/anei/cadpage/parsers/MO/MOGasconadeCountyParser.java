package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchGlobalDispatchParser;



public class MOGasconadeCountyParser extends DispatchGlobalDispatchParser {
  
  private static final Pattern UNIT_PTN = Pattern.compile("(?!911)(?:[GOMB]|BE|BL|MT|NH|SJ)?\\d{2,4}|[A-Z][FP]D|GAAD|GFDTANK|MSHP|<[A-Z0-9]+>");
  
  public MOGasconadeCountyParser() {
    super(CITY_TABLE, "GASCONADE COUNTY", "MO", LEAD_SRC_UNIT_ADDR | PLACE_FOLLOWS_CALL, null, UNIT_PTN);
    setupCallList(CALL_LIST);
    setupSaintNames("LOUIS");
    setupMultiWordStreets(
        "AHR STROM",
        "BALD HILL",
        "BEEMONT SCHOOL",
        "BEM CHURCH",
        "CEDAR LANE",
        "CHAMPION CITY",
        "COUNTY LINE",
        "ELK HEAD",
        "HILLSIDE TRAILER",
        "HOMETOWN PLAZA",
        "INDIAN BEND",
        "LAKE NORTHWOODS",
        "LAKE SHORE",
        "MARIES COUNTY",
        "MT STERLING",
        "NURSING HOME",
        "OAK HAVEN",
        "OCEAN WAVE",
        "PEACEFUL VALLEY",
        "PIGG HOLLOW",
        "PUMP STATION",
        "RED BIRD",
        "RED OAK",
        "ROLLING HOME",
        "STOCK PILE",
        "STONE HILL",
        "TRIPLE E",
        "VIRGIL NICKS"
    );
  }
  
  @Override
  public String getFilter() {
    return "central@fidmail.com,gc911text@gmail.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("MESSAGE / ")) body = body.substring(10).trim();
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("FRNKLN CNTY")) data.strCity = "FRANKLIN COUNTY";
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 ACCIDENTAL",
      "911 HANG UP",
      "911 OPEN LINE",
      "911 TRANSFER TO ANOTHER AGENCY",
      "ABDOMINAL PAIN",
      "ALARM SOUNDING- EMS",
      "ALARM-COMMERCIAL FIRE ALARM",
      "ALARM- RESIDENTIAL FIRE ALARM",
      "ALLERGIC REACTION",
      "ACCIDENTAL INJURY",
      "ASSIST AN INVALID",
      "ASSIST ANOTHER AGENCY- EMS",
      "ASSIST ANOTHER AGENCY - FIRE",
      "ASSIST EMS- FIRE NEEDED",
      "BACK PAIN",
      "BFD FALL",
      "BRUSH - 3 OR LESS ACRES/NO EXPOSURES",
      "BRUSH - 4+ ACRES AND/OR EXPOSURES",
      "CARBON MONOXIDE NO ILLNESS",
      "CARDIAC ARREST",
      "CHECK THE AREA",
      "CHEST PAIN",
      "CHOKING",
      "CONTROLLED BURN",
      "DIABETIC PROBLEMS",
      "DIFFICULTY BREATHING",
      "DOMESTIC IN PROGRESS- PHYSICAL",
      "EYE INJURY/PROBLEMS",
      "FALL",
      "FALL- PERSON FELL",
      "FLUE FIRE CONTAINED",
      "GAS LEAK OUTSIDE",
      "HEADACHE",
      "HEART PROBLEMS",
      "HEMORRHAGE/ BLEEDING",
      "LACERATION",
      "MCI LEVEL 1",
      "MOTOR VEHICLE ACCIDENT-INJURIES",
      "MOTOR VEHICLE ACCIDENT-NO INJURIES",
      "MOTOR VEHICLE ACCIDENT-UNKNOWN INJ",
      "MOTOR VEHICLE ACCIDENT-SINKING VEHICLE",
      "MOTOR VEHICLE ACCIDENT-WITH FIRE",
      "MOTOR VEHICLE ACCIDENT WITH RESCUE",
      "MOVE UP",
      "OBS/ NON VIOLENT",
      "OVERDOSE",
      "PHONE CALL",
      "POISONING",
      "PRISONER TRANSPORT",
      "REKINDLE",
      "SEIZURES",
      "SICK CASE",
      "SICK PERSON- SICK CASE",
      "STROKE",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE- COMMERCIAL",
      "STRUCTURE FIRE- RESIDENTIAL",
      "SUICIDAL PERSON-SCENE NOT SECURE",
      "SUICIDAL PERSON-SCENE SECURE",
      "TRAFFIC STOP",
      "TRANSFER- ROUTINE",
      "TRAUMATIC INJURY",
      "UNCONSCIOUS PERSON- NOW CONSCIOUS",
      "UNKNOWN EMS",
      "UNRESPONSIVE PERSON",
      "UNUSUAL ODOR",
      "VEHICLE FIRE",
      "VEHICLE FIRE WITH EXPOSURES",
      "WALK IN TO AMB BUILDING"
  );

  private static final String[] CITY_TABLE = new String[]{
    "BLAND",
    "BELLE",
    "GASCONADE",
    "GERALD",
    "HERMANN",
    "MORRISON",
    "MT STERLING",
    "OWENSVILLE",
    "ROSEBUD",
    "SULLIVAN",
    
    "FRNKLN CNTY",
    "FRANKLIN COUNTY",
    "GASCONADE COUNTY",
    "MARIES COUNTY",
    "OUT OF COUNTY",
    "OSAGE COUNTY"
  };
}
