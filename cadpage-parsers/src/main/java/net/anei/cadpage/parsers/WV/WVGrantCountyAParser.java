package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

/**
 * Grant County, WV (A)
 */
public class WVGrantCountyAParser extends DispatchA48Parser {

  private static final Pattern UNIT_PTN = Pattern.compile("\\b(?:[A-Z]{1,2}\\d+[A-Z]?|[A-Z]{1,3}EMS|77\\d|FIRST_ENERGY|GRANTCOMM|HELOCOPTER|SOUTHG)\\b", Pattern.CASE_INSENSITIVE);

  public WVGrantCountyAParser() {
    super(CITY_LIST, "GRANT COUNTY", "WV", FieldType.GPS_PLACE_X, A48_NO_CODE, UNIT_PTN);
    setupCallList(CALL_CODE);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "CAD@hardynet.com,PETERSBURGFIRE@hardynet.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MARKER = Pattern.compile("CAD:([A-Za-z_]+) (?:\\d{3}|EMS) TEXT +|([ A-Z]+):");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    String source = null;
    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) {
      source = match.group(1);
      if (source == null) source = match.group(2).trim();
      body = body.substring(match.end());
    }

    body = body.replace(" WV - -", "").replace("FIRST ENERGY", "FIRST_ENERGY");

    match = UNIT_PTN.matcher(body);
    int lastCommaPt = body.indexOf(',');
    int pt = -1;
    while (match.find()) pt = match.end();
    String info = null;
    if (pt > lastCommaPt && pt < body.length() && body.charAt(pt) == ' ') {
      info = body.substring(pt+1).trim();
      body = body.substring(0, pt);
    }

    if (!super.parseMsg(subject, body, data)) return false;

    if (source != null) data.strSource = source;

    if (info != null) data.strSupp = append(data.strSupp, "\n", info);

    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram() + " INFO";
  }

  @Override
  public String adjustMapAddress(String address) {
    address = address.replace("PAPERBACK MAPLE ST", "MAPLE ST");
    return super.adjustMapAddress(address);
  }

  private static final String[] MWORD_STREET_LIST = new String[]{

      "BAYARD CEMETERY",
      "BENSENHAVER HL RIDGE",
      "BIG HILL",
      "BIG RUN",
      "BIG SKY",
      "CAMP ECHO",
      "CHERRY RIDGE",
      "CLARKS VIEW",
      "DEEP SPRING",
      "DOLLY SODS",
      "GAP MOUNTAIN",
      "GEORGE WASHIGNTON",
      "GEORGE WASHINGTON",
      "GOLDEN TROUT",
      "H L",
      "HAVEN FARMS",
      "HENRY DOBBIN",
      "HIGH POINT",
      "HIGH VALLEY",
      "JOHNSON RUN",
      "JORDAN RUN",
      "KUHN MINE",
      "LAKE RETREAT",
      "LAUREL DALE",
      "LAUREL RUN",
      "LIMESTONE HAUL",
      "LIVING SPRINGS",
      "LUNICE CREEK",
      "MAPLE HILL",
      "MARVIN BERGDOLL",
      "MILL CREEK",
      "MISS LIZZY",
      "MOUNTAIN LAKE RETREAT",
      "MOUNTAIN VIEW",
      "NOBLE FIR",
      "PAPERBACK MAPLE",
      "PATTERSON CREEK",
      "PETERSBURG GAP",
      "PINK ALT",
      "POOR FARM",
      "PORT REPUBLIC",
      "POWER STATION",
      "RECREATION SITE",
      "RED BARN",
      "SHOOKS GAP",
      "SITES TOWN",
      "SKY VALLEY",
      "SMOKE HOLE",
      "SPRING RUN",
      "WELTON ORCHARD",
      "WINDY HILL",
      "YUCKY RUN"
  };

  private static final CodeSet CALL_CODE = new CodeSet(
      "ABBACKP",
      "BLEEDNOTRAUMA",
      "BREATHDIFF",
      "BREATHING DIFFICULTY/TROUBLE BREATHING/SHORT OF BREATH-SOB/DIFFICULTY BREATHING",
      "BRUSH FIRE/GRASS FIRE/WOODS FIRE",
      "BRUSH FIRE/GRASS FIRE/WOODS FIRE HARDY CO",
      "CHESTHEART",
      "COALARM",
      "CONTROLLED BURN",
      "DIABET",
      "DIABETIC EMERGENCY / LOW BLOOD SUGAR / HIGH BLOOD SUGAR / GLUCOSE LEVEL",
      "DOM",
      "ELECTRICAL FIRE",
      "EMSASSIST",
      "EMSTRANS",
      "ESTBY",
      "FALL",
      "FIRE ALARM/AUTOMATIC FIRE ALARM/COMMERCIAL FIRE ALARM/RESIDENTIAL FIRE ALARM",
      "FIRE STANDBY",
      "FIREALARM",
      "FLUEFIRE",
      "GYNBIRTH",
      "HIT N RUN",
      "LANDING ZONE SETUP",
      "LMISC",
      "LZSET",
      "MENTAL",
      "MISC",
      "MISC CALL NOT LAW ENFORCEMENT",
      "MOTOR VEHICLE CRASH WITH INJURY OR ENTRAPMET ACCIDENT",
      "MUT AID EMS",
      "MUT AID FIRE",
      "MVC",
      "MVCINJ",
      "ODOR OF GAS",
      "ODPOISON",
      "PROCESS",
      "PUBSER",
      "RDHAZ",
      "SEIZURE",
      "SICK",
      "SMELL ODOR-GAS OUTDOORS",
      "SMOKE",
      "SMOKE INVESTIGATION OUTDOORS",
      "STABBING/GUNSHOT",
      "STROKE",
      "STROKE/SLURRED SPEECH/DROOPING",
      "STRUCTURE FIRE",
      "TEST CALL",
      "TRAUMA",
      "TRAUMATIC INJURY",
      "TS",
      "UNCONS UNRESPONSIVE",
      "UNCONSCIOUS/UNRESPONSIVE/",
      "UNCONSCIOUS/UNRESPONSIVE/SYNCOPE",
      "UTILITY",
      "VEHICLE ACCIDENT-NO INJURY OR ENTRAPMENT MOTOR VEHCLE CRASH",
      "VEHICLE FIRE",
      "WELFARE"
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
      "KEYSER",
      "NEW CREEK",

      // Pendleton County
      "UPPER TRACT"

  };
}
