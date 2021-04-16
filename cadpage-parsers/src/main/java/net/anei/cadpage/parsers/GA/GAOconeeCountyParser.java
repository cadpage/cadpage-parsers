package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;


public class GAOconeeCountyParser extends DispatchA48Parser {

  public GAOconeeCountyParser() {
    super(CITY_LIST, "OCONEE COUNTY", "GA", FieldType.PLACE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets(
        "BARROW COUNTY LINE",
        "BENT TREE PT");
  }

  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_RECHECK_APT;
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT (INJURIES",
      "ANIMAL CASE",
      "CHASE",
      "DISCHARGING FIREARMS",
      "DOMESTIC PHYSICAL",
      "DOMESTIC PHYSICAL",
      "DOMESTIC VERBAL",
      "DRUG/DRUG RELATED",
      "ENTERING AUTO",
      "FIGHTING OR DISORDER",
      "FIRE",
      "FIRE ALARM",
      "FIRE STRUCTURE",
      "GAS LEAK",
      "INJURED PERSON",
      "LIFT ASSITANCE",
      "MEDICAL ALARM",
      "NEGLECT/SEXUAL ABUSE TO CHILD",
      "PRIVATE PROPERTY / INJURY",
      "SICK PERSON",
      "SIGN DOWN/DAMAGED",
      "SMOKE",
      "TRAFFIC LIGHT OUT",
      "TREE DOWN IN POWER LINES",
      "TREE DOWN ON ROAD"
  );

  private static final String[] MWORD_STREET_LIST = new String[] {
      "ATHENS RIDGE",
      "BARBER CREEK",
      "BARNETT SHOALS",
      "BLACK OAK",
      "CLIFF DAWSON",
      "COLHAM FERRY",
      "DANIELLS BRIDGE",
      "DIALS MILL",
      "DOUBLE BRIDGES",
      "EPPS BRIDGE",
      "EXPERIMENT STATION",
      "FLAT ROCK",
      "HARDEN HILL",
      "HIGH SHOALS",
      "HODGES MILL",
      "HOG MOUNTAIN",
      "HOLLOW CREEK",
      "JENNINGS MILL",
      "JIMMY DANIEL",
      "JIMMY DANIELL",
      "JIMMY DANIELS",
      "KNOB CREEK",
      "LAKE WELBROOK",
      "LANE CREEK",
      "LIVE OAK",
      "MALCOM BRIDGE",
      "MARS HILL",
      "MCNUTT CREEK",
      "MOORES FORD",
      "NORTH BURSON",
      "OCONEE SPRINGS",
      "OLIVER BRIDGE",
      "PERSIMMON CREEK",
      "PRICE MILL",
      "RIVER'S EDGE",
      "RIVERS EDGE",
      "ROCKY BRANCH",
      "SCARLET OAK",
      "TIMBER RIDGE",
      "TWIN OAKS",
      "UNION CHURCH",
      "VIRGIL LANGFORD",
      "WILD FLOWER"
  };


  private static final String[] CITY_LIST = new String[] {
      "ATHENS",
      "BISHOP",
      "BOGART",
      "FARMINGTON",
      "NORTH HIGH SHOALS",
      "STATHAM",
      "WATKINSVILLE"
  };
}
