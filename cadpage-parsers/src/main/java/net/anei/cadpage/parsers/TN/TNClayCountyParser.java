package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class TNClayCountyParser extends DispatchB2Parser {

  public TNClayCountyParser() {
    super(CITY_LIST, "CLAY COUNTY", "TN");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "AMOS MCLERRAN",
        "B THOMPSON",
        "BETT BUFORD",
        "BILLIE HILL",
        "CLAY COUNTY",
        "DRY MILL CREEK",
        "FIRE HALL",
        "JAMES WHITE",
        "JIM SHORT",
        "JOE STONE",
        "KNOB CREEK",
        "MAYFIELD BROWN",
        "MOSS ARCOT",
        "NEELEY CREEK",
        "PEA RIDGE FIRETOWER",
        "ROCK SPRINGS",
        "ROSS BOLES",
        "SCOTT HOLLOW",
        "UNION HILL"
    );
  }
  
  @Override
  protected boolean isPageMsg(String body) {
    return body.contains(">") && body.contains(" Cad:");
  }

  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_AT_MEANS_CROSS;
  }


  private static final CodeSet CALL_LIST = new CodeSet(
      "4-WHEELER/MOTORCYCLE/ATV",
      "ALARM",
      "AMBULANCE NEEDED",
      "ANIMAL OR LIVESTOCK CALL",
      "BUSY",
      "CHEST PAIN",
      "DEBRIS IN ROADWAY",
      "DIABETIC", 
      "DOMESTIC VIOLENCE",
      "DOMESTIC W/WEAPONS",
      "DRIVING WHILE DRUNK",
      "FALL",
      "HEART ATTACK",
      "LARCENY",
      "MOTORIST ASSIST",
      "PROWLER",
      "PUBLIC DRUNK",
      "RAPE",
      "RECKLESS DRIVER",
      "SEIZURES",
      "SERVING WARRANT",
      "SHORT OF BREATH",
      "SICKNESS (GENERAL)",
      "STROKE",
      "SUSPICIOUS VEHICLE",
      "TEST CALL",
      "UNLOCK CAR DOOR",
      "VEHICLE ACCIDENT-INJURY", 
      "VEHICLE ACCIDENT-PD",
      "WANT OFFICER",
      "WATER OUTAGE",
      "WELFARE CHECK"
  );

  private static final String[] CITY_LIST = new String[]{
    "ARCOT",
    "BEECH BETHANY",
    "BRIMSTONE",
    "BUTLER'S LANDING",
    "CELINA",
    "DENTON CROSSROADS",
    "FREE HILL",
    "HAMILTON BRANCH",
    "HERMITAGE SPRINGS",
    "HILHAM",
    "LILY DALE",
    "MAPLE GROVE",
    "MCLERRAN",
    "MOSS",
    "NEELY CROSSROADS",
    "OAK GROVE",
    "RED BOILING SPRINGS",
    "PEA RIDGE",
    "SHANKY BRANCH", 
    "WHITLEYVILLE"
  };
}
