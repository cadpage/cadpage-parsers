package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

/**
 * Fayette County, Pennsylvania
 */
public class PAFayetteCountyParser extends DispatchB2Parser {

  public PAFayetteCountyParser() {
    super("FAYETTE-911:", -5, CITY_LIST, "FAYETTE COUNTY", "PA");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "FAYETTE-911@fcema.org";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL_ADDR")) return new MyCallAddressField();
    return super.getField(name);
  }
  
  private class MyCallAddressField extends BaseCallAddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "4TH EASY",
      "AIRWAY INN",
      "ARCH BRIDGE",
      "BALL PARK",
      "BARTON MILL",
      "BELL VIEW",
      "BEN LOMOND",
      "BUENA VISTA",
      "BULL RUN",
      "CAMP CARMEL",
      "CANAAN CHURCH",
      "CENTRAL SCHOOL",
      "CHALK HILL OHIOPYLE",
      "CHERRY TREE",
      "CRAFT MOORE",
      "DICKERSON RUN",
      "DINNER BELL FIVE FORKS",
      "DRY HILL",
      "DUCK HOLLOW",
      "FAIRBANK HERBERT",
      "FAN HOLLOW",
      "FARMINGTON OHIOPYLE",
      "FAYETTE CITY",
      "FAYETTE SPRINGS",
      "FILBERT ORIENT",
      "FIRE SCHOOL",
      "FIRE TOWER",
      "FLAT ROCK",
      "FORKY FORD",
      "FORT HILL",
      "FURNACE HILL",
      "GILLESPIE HOLLOW",
      "GREEN JUNCTION",
      "GUN CLUB",
      "HICKORY SQUARE",
      "HIGHLAND PARK",
      "HILL FARM",
      "INDIAN HEAD",
      "INDUSTRIAL PARK",
      "KINGIN HILL",
      "LAMBERT FOOTEDALE",
      "LAURITA HILL",
      "LECKRONE HIGHOUSE",
      "LECKRONE MASONTOWN",
      "LIMESTONE HILL",
      "LINDEN HALL",
      "LITTLE SUMMIT",
      "MARY HALL",
      "MASON DIXON",
      "MCCOY HOLLOW",
      "MEADOW RUN",
      "MILL RUN",
      "MOUNT VERNON",
      "MT PLEASANT CONNELLSVILLE",
      "NARROWS HILL",
      "NAVAHO HOLLOW",
      "OAK CREEK",
      "PALMER ADAH",
      "PLEASANT VALLEY",
      "PLEASANT VIEW SMOCK",
      "QUAIL HILL",
      "REDSTONE CHURCH",
      "REHOBOTH CHURCH",
      "ROCKY RUN",
      "ROUND BARN",
      "ROUTE 51",
      "RT 21",
      "SANDY FLAT",
      "SCOTTDALE SMITHTON",
      "SEARIGHTS HERBERT",
      "SHIMKO HOLLOW",
      "SMITH SCHOOL",
      "STONE CHURCH",
      "STONEY POINT",
      "STREET CAR",
      "SUFF RIDGE",
      "SUGAR LOAF",
      "SUNSHINE HOLLOW",
      "SWINK HILL",
      "TANYARD HOLLOW",
      "THOMPSON 1",
      "THOMPSON 2",
      "TOWER HILL",
      "TOWN COUNTRY",
      "VALLEY FORGE",
      "VANCES MILL",
      "WATER TANK",
      "WHARTON FURNACE",
      "YAUGER HOLLOW"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "AED CARDIAC ARREST ALS",
      "AED SEIZURE ALS",
      "AED UNRESPONSIVE ALS",
      "AUTOMATIC FIRE ALARM COMERCIAL",
      "AUTOMATIC FIRE ALARM RESIDENCE",
      "CARDIAC ARREST ALS",
      "E ABDOMINAL PAIN ALS",
      "E CHEST PAINS ALS",
      "E DIABETIC ALS",
      "E FALLS ALS",
      "E HEADACHE ALS",
      "E MED ALARM ALS",
      "E POISON/OVERDOSE ALS",
      "E PUBLIC SERVICE BLS",
      "E SEIZURE ALS",
      "E SICK/UNKNOWN ALS",
      "E STROKE ALS",
      "E TRAUMA ALS",
      "E UNRESP/UNCONSC/SYNCOPE ALS",
      "E_ASSIST",
      "E_BACK PAIN ALS",
      "E_BLEEDING ALS",
      "E_BREATHING ALS",
      "F-ACCIDENT W/ INJURIES",
      "F_ACCIDENT NO INJURIES",
      "F_ACCIDENT W/ ENTRAPTMENT",
      "F_ASSIST",
      "F_BRUSH FIRE",
      "F_CARBON MONOXIDE NO SYMPTOMS",
      "F_CARBON MONOXIDE W/ SYMPTOMS",
      "F_CHIMNEY FIRE",
      "F_CONTROL BURN",
      "F_DUMPSTER FIRE",
      "F_ELECTRICAL FIRE",
      "F_EXPLOSION 2",
      "F_FLOODING",
      "F_FUEL SPILL",
      "F_GAS LEAK",
      "F_ILLEGAL BURN",
      "F_LANDING ZONE",
      "F_MISC FIRE",
      "F_MULT CALLS",
      "F_ODOR INVESTIGATION",
      "F_PEDESTRIAN ACCIDENT",
      "F_PHYSICAL RESCUE",
      "F_REKINDLE",
      "F_ROAD HAZZARD",
      "F_STRUCTURE FIRE COMMERCIAL",
      "F_STRUCTURE FIRE",
      "F_TRAFFIC CONTROL",
      "F_TRASH FIRE",
      "F_TREE DOWN",
      "F_UNKNOWN FIRE",
      "F_WIRES DOWN",
      "H HUMAN SERVICES",
      "P CHECK ON WELFARE",
      "P DISABLED VEHICLE",
      "P DOMESTIC",
      "P MULTIPLE CALLS",
      "P SUSPICIOUS ACTIVITY",
      "P TRAFFIC HAZARD",
      "SMOKE STRUCTURE",
      "TEST CALL",
      "VEHICLE FIRE"

  );
  
  private static final String[] CITY_LIST = new String[]{
      "CONNELLSVILLE",
      "CONNELLSVILLE CITY",
      "UNIONTOWN",
  
      "BELLE VERNON",
      "BROWNSVILLE",
      "DAWSON",
      "DUNBAR",
      "EVERSON",
      "FAIRCHANCE",
      "FAYETTE CITY",
      "MARKLEYSBURG",
      "MASONTOWN",
      "NEWELL",
      "OHIOPYLE",
      "PERRYOPOLIS",
      "POINT MARION",
      "SEVEN SPRINGS",
      "SMITHFIELD",
      "SOUTH CONNELLSVILLE",
      "VANDERBILT",
    
      "BROWNSVILLE TWP",
      "BULLSKIN",
      "CONNELLSVILLE TWP",
      "DUNBAR TWP",
      "FRANKLIN",
      "GEORGES",
      "GERMAN",
      "HENRY CLAY",
      "JEFFERSON",
      "LOWER TYRONE",
      "LUZERNE",
      "MENALLEN",
      "NICHOLSON",
      "NORTH UNION",
      "PERRY",
      "REDSTONE",
      "SALTLICK",
      "SOUTH UNION",
      "SPRINGFIELD",
      "SPRINGHILL",
      "STEWART",
      "UPPER TYRONE",
      "WASHINGTON",
      "WHARTON"
  };
}
