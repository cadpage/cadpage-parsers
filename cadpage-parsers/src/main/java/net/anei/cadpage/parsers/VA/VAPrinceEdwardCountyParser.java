package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;

/**
 * Prince Edward County, VA
 */
public class VAPrinceEdwardCountyParser extends DispatchDAPROParser {
  
  public VAPrinceEdwardCountyParser() {
    super(CITY_CODE_TABLE, "PRINCE EDWARD COUNTY", "VA");
    setupCallList(CALL_SET);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@farmvilleva.com";
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "ASPEN HILL",
      "BACK H-S",
      "BACK HAMPDEN SYDNEY",
      "CAMPBELL CROSSING",
      "CAMPBELL HILL",
      "CARY SHOP",
      "FIVE FORKS",
      "GATES BASS",
      "GOOD HOPE",
      "HEIGHTS SCHOOL",
      "INDUSTRIAL PARK",
      "JAMES MADISON",
      "MILNWOOD VILLAGE",
      "MOUNTAIN CREEK",
      "MT PLEASANT",
      "OAK RIDGE",
      "OLE BRIERY STATION",
      "OLIVE BRANCH",
      "PATRICK HENRY",
      "PIN OAK",
      "POPLAR HILL",
      "PRINCE EDWARD",
      "RATTLERS BRANCH",
      "SAYLERS CREEK",
      "SCHULTZ MILL",
      "WATER WORKS",
      "ZION HILL"
  };  
  private static final CodeSet CALL_SET = new CodeSet(
      "ABDOMINAL/BACK PAIN",
      "ACCIDENT NO INJURY",
      "ACCIDENT WITH INJURIES",
      "ALLERGIC REACTION",
      "ALTERED LOC / CONFUSED / DISOR",
      "ASSIST LAW ENFORCEMENT",
      "AVIATION EMERGENCY",
      "BLEEDING",
      "BRUSH / FOREST FIRE",
      "BURGLAR ALARM / PANIC ALARMS",
      "CARDIAC CALL, NO CHEST PAIN",
      "CHEST PAINS",
      "COMMERCIAL STRUCTURE FIRE",
      "COMMERCIAL STRUCTURE FIRE HSC /",
      "DEAD BODY/NOTIFY MED.EXAM.",
      "DIABETIC",
      "DIFFICULTY BREATHING",
      "DISABLED VEHICLE",
      "DUMPSTER FIRE",
      "EMS CALL (UNKNOWN PROBLEM)",
      "FALL",
      "FIRE (UNKNOWN)",
      "FIRE ALARM",
      "FIRE CONTROL BURN",
      "FULL CARDIAC ARREST",
      "GYNECOLOGY/MISCARRIAGE",
      "HAZMAT/SPILL",
      "MEDICAL ALARM",
      "ODOR INVESTIGATION",
      "ORTHOPEDIC CALL - BONE RELATED",
      "OVERDOSE/POISONING",
      "PAIN RELATED / NOTE SPECIFICS",
      "PREGNANCY/CHILDBIRTH",
      "PUBLIC ASSISTANCE REQUEST",
      "PUBLIC ASSISTANCE (RESCUE)",
      "REPORT",
      "RESCUE CALL",
      "SEIZURES",
      "SHOOTING",
      "SICK",
      "SMOKE INVESTIGATION",
      "SMOKE INVESTIGATION (OUTSIDE)",
      "STAND-BY CALLS",
      "STROKE",
      "STRUCTURE FIRE",
      "SUICIDE",
      "SUSP INDIVIDUAL / PERSONS",
      "TRAFFIC HAZARD",
      "TRAINING RELATED EVENT",
      "TRANSFER / INTERFACILITY / PAL",
      "TRASH FIRE",
      "TRASH FIRE (RUBBISH)",
      "TREE DOWN",
      "UNCONSCIOUS",
      "VEHICLE/EQUIPMENT FIRE",
      "WIRE / TRANSFORMER FIRE"
  );
  
  private static final Properties CITY_CODE_TABLE =
    buildCodeTable(new String[]{
        "FAR", "FARMVILLE",
        "GRE", "GREEN BAY",
        "HSC", "HAMPDEN SYDNEY",
        "KEY", "KEYSVILLE",
        "MEH", "MEHERRIN",
        "PAM", "PAMPLIN CITY",
        "PRO", "PROSPECT",
        "RIC", "RICE"
    });
}