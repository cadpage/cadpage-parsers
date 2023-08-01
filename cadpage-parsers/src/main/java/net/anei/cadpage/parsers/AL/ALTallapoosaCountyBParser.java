package net.anei.cadpage.parsers.AL;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALTallapoosaCountyBParser extends FieldProgramParser {

  public ALTallapoosaCountyBParser() {
    super(CITY_LIST, "TALLAPOOSA COUNTY", "AL",
          "CALL CALL_EXT/CS? ADDR/S6! CITY? INFO/N+");
  }

  private static final Pattern DELIM = Pattern.compile("[;,:]");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(DELIM.split(body), data)) return false;
    return !data.strCity.isEmpty() ||
           VALID_CALLS.contains(data.strCall.toUpperCase()) ||
           checkAddress(data.strAddress) >= STATUS_INTERSECTION;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL_EXT"))  return new CallField("LIFT ASSIST", true);
    return super.getField(name);
  }

  private static final Set<String> VALID_CALLS = new HashSet<>(Arrays.asList(
      "10-25 ACCIDENT",
      "10-25 FIRE ALARM",
      "10-25 MISSING PERSON",
      "10-25 VEHICLE FIRE",
      "10-50",
      "10-70 STRUCTURE",
      "ACCIDENT",
      "AGENCY ASSIST",
      "ALTERCATION W/ INJURIES",
      "ASSAULT",
      "ASSIST",
      "BRUSH FIRE",
      "CANCEL ACCIDENT",
      "CANCEL FIRE",
      "CANCEL FIRE ALARM",
      "CITIZEN ASSIST",
      "ELEVATOR STUCK",
      "EMS",
      "FIRE",
      "FIRE ALARM",
      "FIRE INVESTIGATION",
      "FLARE UP",
      "GAS LINE BUSTED",
      "GRASS FIRE",
      "HAZARD",
      "LANDING ZONE",
      "LANDING ZONE SET UP",
      "LIFT ASSIST",
      "LIST ASSIST",
      "LZ",
      "LZ SET UP",
      "MEDCAL",
      "MEDICAL",
      "MEDICAL ALARM",
      "MEDICAL, LIFT ASSIST",
      "MEDICLAL",
      "MISSING PERSON",
      "POWER LINE DOWN",
      "POWER LINE DOWN/FIRE",
      "RESCUE",
      "SMELL OF GAS",
      "SMOKE INVESTIGATION",
      "STRUCTURE FIRE",
      "TRAFFIC HAZARD",
      "TRANSFORMER FIRE",
      "TREE DOWN",
      "TREE IN ROADWAY",
      "TREES IN ROADWAY",
      "UNKOWN SMELL/ALARM",
      "UPDATE",
      "VEHICLE",
      "VEHICLE FIRE",
      "WRECK"
  ));

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "ALEXANDER CITY",
      "DADEVILLE",
      "TALLASSEE",

      // Towns
      "CAMP HILL",
      "DAVISTON",
      "GOLDVILLE",
      "JACKSONS GAP",
      "NEW SITE",

      // Census-designated places
      "HACKNEYVILLE",
      "OUR TOWN",
      "REELTOWN",

      // Unincorporated communities
      "ANDREW JACKSON",
      "BULGERS",
      "CHEROKEE BLUFFS",
      "CHURCH HILL",
      "DUDLEYVILLE",
      "FOSHEETON",
      "FROG EYE",

      // Lee County
      "NOTASULGA"
  };
}
