package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;



public class KYHarrisonCountyBParser extends DispatchB2Parser {
  
  public KYHarrisonCountyBParser() {
    super("HARRISON_COUNTY_911:", CITY_LIST, "HARRISON COUNTY", "KY");
    setupMultiWordStreets(
        "BOWMANS MILL",
        "DIVIDING RIDGE",
        "FARMER TRAIL",
        "GRAYS RUN",
        "HINTON SADIEVILLE",
        "INDIAN CREEK",
        "KELLER WAITS",
        "LICKING VALLEY",
        "MOUTH OF CEDAR",
        "NEWTOWN LEESBURG",
        "OAK RIDGE",
        "ODDVILLE SUNRISE",
        "OFF ANTIOCH RICHLAND",
        "SNAKE LICK",
        "STRINGTOWN WEBBER",
        "WAGNORS CHAPEL"
    );
  }
  
  @Override
  public String getFilter() {
    return "HARRISON_COUNTY_911@cynthianaky.com";
  }
  
  private static final String[] CITY_LIST = new String[]{

    "BERRY",
    "CORINTH",
    "CYNTHIANA",

//Unincorporated communities

    "BOYD",
    "BRECKINRIDGE",
    "BROADWELL",
    "BUENA VISTA",
    "COLVILLE",
    "CONNERSVILLE",
    "HOOKTOWN",
    "LAIR",
    "LEES LICK",
    "LEESBURG",
    "KELAT",
    "MORNINGGLORY",
    "ODDVILLE",
    "POINDEXTER",
    "RUDDELS MILLS",
    "RUTLAND",
    "SHADYNOOK",
    "SHAWHAN",
    "SUNRISE"
  };
 
  @Override
  protected CodeSet buildCallList() {
    return new CodeSet(
        "CONTROL BURN",
        "MEDICAL EMERGENCY",
        "SICK PERSON",
        "STROKE / CVA",
        "ODOR/GAS INVESTIGATION",
        "UNCONSCIOUS / FAINTING",
        "BREATHING PROBLEMS", 
        "CHEST PAIN",
        "BACK PAIN",
        "FALL ON PRIVATE PROPERTY",
        "STRUCTURE FIRE",
        "LOCKOUT VEHICLE -HOUSE",
        "SEIZURE-CONVULSIONS",
        "FIRE (UNSPECIFIED OR UNKNOWN)",
        "DIABETIC PROBLEMS",
        "BLEEDING LACERATIONS",
        "ACCIDENT W/INJURIES",
        "POISONING / INGESTION"
    );
 }
}