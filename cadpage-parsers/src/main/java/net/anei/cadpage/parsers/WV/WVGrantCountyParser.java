package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

/**
 * Hardy County, WV (B) 
 */
public class WVGrantCountyParser extends DispatchA48Parser {
  
  public WVGrantCountyParser() {
    super(CITY_LIST, "GRANT COUNTY", "WV", FieldType.NONE);
    setupCallList(CALL_CODE);
  }
  
  @Override
  public String getFilter() {
    return "CAD@hardynet.com";
  }
  
  private static final CodeSet CALL_CODE = new CodeSet(
      "BREATHING DIFFICULTY/TROUBLE BREATHING/SHORT OF BREATH-SOB/DIFFICULTY BREATHING",
      "BRUSH FIRE/GRASS FIRE/WOODS FIRE",
      "BRUSH FIRE/GRASS FIRE/WOODS FIRE HARDY CO",
      "DIABETIC EMERGENCY / LOW BLOOD SUGAR / HIGH BLOOD SUGAR / GLUCOSE LEVEL",
      "ELECTRICAL FIRE",
      "FALL",
      "FIRE ALARM/AUTOMATIC FIRE ALARM/COMMERCIAL FIRE ALARM/RESIDENTIAL FIRE ALARM",
      "MISC CALL NOT LAW ENFORCEMENT",
      "MOTOR VEHICLE CRASH WITH INJURY OR ENTRAPMET ACCIDENT",
      "SMELL ODOR-GAS OUTDOORS",
      "SMOKE INVESTIGATION OUTDOORS",
      "STROKE/SLURRED SPEECH/DROOPING",
      "TEST CALL",
      "TRAUMATIC INJURY",
      "UNCONSCIOUS/UNRESPONSIVE/",
      "UNCONSCIOUS/UNRESPONSIVE/SYNCOPE",
      "VEHICLE ACCIDENT-NO INJURY OR ENTRAPMENT MOTOR VEHCLE CRASH",
      "VEHICLE FIRE"
  );
  
  private static final String[] CITY_LIST = new String[]{

      //CITY

          "PETERSBURG",

      //TOWN

          "BAYARD",

      //UNINCORPORATED COMMUNITIES

          "ARTHUR",
          "BISMARCK",
          "CABINS",
          "DOBBIN",
          "DORCAS",
          "FAIRFAX",
          "FORMAN",
          "GORMANIA",
          "GREENLAND",
          "HENRY",
          "HOPEVILLE",
          "LAHMANSVILLE",
          "MAYSVILLE",
          "MEDLEY",
          "MOUNT STORM",
          "OLD ARTHUR",
          "SCHERR",
          "WILLIAMSPORT",
          "WILSONIA",
          
      // Mineral County
          "BURLINGTON",
          "NEW CREEK"

  };
}
