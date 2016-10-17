package net.anei.cadpage.parsers.NY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

/**
 * Allegany County, NY
 */
public class NYAlleganyCountyParser extends DispatchA48Parser {
  
  public NYAlleganyCountyParser() {
    super(CITY_LIST, "ALLEGANY COUNTY", "NY", FieldType.X, 
          Pattern.compile("\\d{3}|[A-Z]{3}\\d{1,3}|4A\\d{1,3}"));
    setupCallList(CALL_LIST);
  }
  
  @Override
  public String getFilter() {
    return "@AC911.com";
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ADMINISTRATIVE/DOCUMENT/LOST OR FOUND PROPERTY/MESSAGES",
      "ALARMS",
      "ASSIST OTHER AGENCIES",
      "CARBON MONOXIDE/INHALATION/HAZMAT/CBRN ALS",
      "CARBON MONOXIDE/INHALATION/HAZMAT/CBRN BLS",
      "CITIZEN ASSIST/SERVICE CALL",
      "CONVULSIONS/SEIZURES ALS",
      "ELECTRICAL HAZARD",
      "FALLS BLS",
      "FUEL SPILL",
      "MUTUAL AID/ASSIST OUTSIDE AGENCY",
      "ODOR (STRANGE/UNKNOWN)",
      "OUTSIDE FIRE / GRASS / WOODS",
      "SICK PERSON(SPECIFIC DIAGNOSIS) BLS",
      "STAND-BY (FIRE)",
      "STRUCTURE FIRE",
      "TEST CALL",
      "TRAFFIC/TRANSPORTATION ACCIDENT",
      "TRAFFIC/TRANSPORTATION INCIDENTS(INJURY) ALS",
      "TRAFFIC VIOLATION/COMPLAINT/HAZARD-TREES/LINES DOWN",
      "TRAINING / DRILL (FIRE)",
      "TRAUMATIC INJURIES(SPECIFIC) BLS",
      "VEHICLE FIRE"
  );
  
  private static final String[] CITY_LIST = new String[]{
        "ALFRED",
        "ALFRED VILLAGE",
        "ALLEN",
        "ALMA",
        "ALMOND",
        "ALMOND VILLAGE",
        "AMITY",
        "ANDOVER",
        "ANDOVER VILLAGE",
        "ANGELICA",
        "ANGELICA VILLAGE",
        "BELFAST",
        "BELMONT",
        "BIRDSALL",
        "BLACK CREEK",
        "BOLIVAR",
        "BOLIVAR VILLAGE",
        "BURNS",
        "CANASERAGA",
        "CANEADEA",
        "CENTERVILLE",
        "CLARKSVILLE",
        "CUBA",
        "CUBA VILLAGE",
        "FILLMORE",
        "FRIENDSHIP",
        "GENESEE",
        "GRANGER",
        "GROVE",
        "HOUGHTON",
        "HUME",
        "INDEPENDENCE",
        "NEW HUDSON",
        "RICHBURG",
        "RUSHFORD",
        "SCIO",
        "STANNARDS",
        "SWAIN",
        "WARD",
        "WELLSVILLE",
        "WELLSVILLE VILLAGE",
        "WEST ALMOND",
        "WILLING",
        "WIRT",
        
        // Cattaraugus County
        "HINSDALE",
        "ISCHUA"
  };
}
