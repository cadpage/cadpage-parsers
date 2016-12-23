package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class SCBerkeleyCountyParser extends DispatchB2Parser {

  public SCBerkeleyCountyParser() {
    super(CITY_LIST, "BERKELEY COUNTY", "SC");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "BETSY HOLE",
        "BLACK OAK",
        "CANADY BRANCH",
        "CANE BAY",
        "CHEROKEE VALLEY",
        "CLAYTON WOODS",
        "CLEAR SKY",
        "COLLEGE PARK",
        "COLONEL MAHAM",
        "COMMERCE CREEK",
        "COUNTY LINE",
        "CYPRESS GARDENS",
        "DAWSON BRANCH",
        "DROP OFF",
        "FARM SPRINGS",
        "GREEN HILL",
        "HORT STAY",
        "LINDERA PRESERVE",
        "LONG NEEDLE",
        "MENDEL RIVERS",
        "M L KING JR",
        "MOUNTAIN PINE",
        "MURPHY BAY",
        "MURRELL BLOCK",
        "NEW HWY",
        "NORTH LIVE OAK",
        "NORTH MAIN",
        "N MAIN",
        "OLD BLACK OAK",
        "OLD DAIRY",
        "OLD FORT",
        "OLD HWY",
        "OLDS WHITESVILLE",
        "PIGEON BAY",
        "RED LEAF",
        "SANTEE RIVER",
        "S LIVE OAK",
        "SOUTH LIVE OAK",
        "SPIERS LANDING",
        "SPRING PLAINS",
        "STARKS CROKER",
        "SWEET BAY",
        "TRANQUIL WATERS",
        "TURKEY CALL",
        "TURTLE POND",
        "WEST END"


    );
   }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      
      "ABDOMINAL PAINS",
      "ASSAULT",
      "AUTO VS PEDESTRIAN",
      "CHEST PAIN",
      "CUTS AND BRUISES",
      "DIABETIC",
      "DRILL/STANDBY",
      "BITE - STING",
      "DIZZINESS - FAINTING",
      "DOA - REQUEST CORONER",
      "EPILEPSY - SEIZURE",
      "FALL WITH INJURY",
      "FIRE ALARM",
      "FIRE CAR",
      "FIRE DUMPSTER",
      "FIRE OTHER",      
      "FIRE STRUCTURE",
      "FIRE WOODS-GRASS-BRUSH",
      "FULL ARREST",
      "GAS LEAK",
      "HEMORRHAGE",
      "LIFT ASSIST/NO INJURY",
      "MEDICAL ALARM",
      "MENTALLY ILL",
      "MISC EMS CALL FOR SERVICE",
      "NAUSEAU VOMITING",
      "OB-GYN EMERGENCY",
      "OVERDOSE",
      "POISONING",
      "RESPIRATORY",
      "SHOOTING",
      "SMOKE INVESTIGATION",
      "SPECIAL DUTY",
      "STROKE",
      "SUICIDE",
      "TRAUMATIC INJURY",
      "UNCONSCIOUS", 
      "UNRESPONSIVE PATIENT",
      "UNSPECIFIED EMERGENCY EMS",
      "WATER MISSION",
      "WRECK"
      
  );

  private static final String[] CITY_LIST = new String[]{

      //Cities
      "CHARLESTON",
      "GOOSE CREEK",
      "HANAHAN",
      "NORTH CHARLESTON",

      //Towns

      "BONNEAU",
      "JAMESTOWN",
      "MONCKS CORNER",
      "ST. STEPHEN",
      "SUMMERVILLE",

      //Townships
      
      "CROSS",
      "GUMVILLE",
      "LADSON",
      "PINOPOLIS"

  };
}
