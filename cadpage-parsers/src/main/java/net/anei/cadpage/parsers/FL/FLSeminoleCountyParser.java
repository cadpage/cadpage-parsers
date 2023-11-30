package net.anei.cadpage.parsers.FL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;

public class FLSeminoleCountyParser extends FieldProgramParser {

  public FLSeminoleCountyParser() {
    super(CITY_LIST, "SEMINOLE COUNTY", "FL",
          "ADDR/SCP TAC:CH MpBk:MAP JZ:MAP/L DISPATCH_TIME:TIME UNITS:UNIT END");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MW_STREET_LIST);
    setupSaintNames("JOHNS");
    removeWords("COVE", "LANE");
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[, ]*\\[\\d\\] *");

  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("CAD Page")) return false;

    // If body starts with Comment: see if can identify
    // a normal alert following the comment.
    boolean leadComment = body.startsWith("Comment:");
    if (leadComment) {
      int pt = 7;
      while (true) {
        pt = body.indexOf(',', pt+1);
        if (pt < 0) {
          setFieldList("INFO");
          data.strSupp = body.substring(8).trim();
          return true;
        }
        String tmp = stripFieldStart(stripFieldStart(body.substring(pt+1), "z"), "y");
        if (CALL_LIST.getCode(tmp) != null) {
          data.strSupp = body.substring(8, pt).trim();
          body = tmp;
          break;
        }
      }
    }

    String info = null;
    int pt = body.indexOf(" _[1]");
    if (pt >= 0) {
      info = body.substring(pt+5).trim();
      body = body.substring(0, pt).trim();
    }
    if (!super.parseMsg(body, data)) return false;
    if (info != null) {
      data.strSupp = INFO_BRK_PTN.matcher(info).replaceAll("\n");
    }

    // No fields are required because leading comment might crowd out
    // everything.  But if there was no lead comment field, everything
    // is required
    return leadComment || !data.strMap.isEmpty();
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " INFO";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new MyTimeField();
    return super.getField(name);
  }

  private static final Pattern TRAIL_APT_PTN = Pattern.compile("(.*)(?: #|, APT *)(\\S+)");
  private static final Pattern APT_PLACE_PTN = Pattern.compile("(?:APT|UNIT) ([A-Z0-9]+?)[- ]+(.*)");
  private static final Pattern APT_PTN = Pattern.compile("\\d+[A-Z]?|[A-Z]?|(?:APT|UNIT) *(\\S*)");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      field = stripFieldStart(field, "z");
      field = stripFieldStart(field, "y");

      String trailApt = "";
      Matcher match = TRAIL_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        trailApt = match.group(2);
      }
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt >= 0) {
          data.strCross = field.substring(pt+1, field.length()-1).trim();
          data.strCross = stripFieldStart(data.strCross, "/");
          field = field.substring(0,pt).trim();
        }
      }

      super.parse(field, data);

      String place = data.strPlace;
      match = APT_PLACE_PTN.matcher(place);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        place = match.group(2).trim();
      } else if ((match = APT_PTN.matcher(place)).matches()) {
        String apt = match.group(1);
        if (apt != null) place = apt;
        data.strApt = append(data.strApt, "-", place);
        place = "";
      }
      data.strPlace = place;

      data.strApt = append(data.strApt, "-", trailApt);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }

  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");

  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      if (!setTime(TIME_FMT, field, data)) abort();
    }
  }

  private static Pattern SHADY_HOLW_PTN = Pattern.compile("\\bSHADY +HOLW\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String sAddress) {
    return SHADY_HOLW_PTN.matcher(sAddress).replaceAll("SHADY HOLLOW LN");
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "2ND ALARM - STRUCTURE FIRE RESIDENTIAL",
      "ABDOMINAL PAIN - FAINTING OR NEAR FAINTING >=50",
      "ALERT 1A",
      "ANIMAL BITES",
      "ASSAULT - OVERRIDE",
      "BACK PAIN - OVERRIDE",
      "BREATHING PROBLEMS",
      "BRUSH FIRE",
      "CARBON MONOXIDE / INHALATION / HAZMAT",
      "CARDIAC ARREST",
      "CHEST PAIN-D",
      "COMMAND ESTABLISHED NATURAL / PROPANE GAS LEAK OUTSIDE",
      "COMMAND ESTABLISHED REF NATURAL / PROPANE GAS LEAK INSIDE",
      "COMMAND ESTABLISHED REF STRUCTURE FIRE RESIDENTIAL",
      "COMMAND ESTABLISHED REF TRAFFIC / TRANSPORTATION ACCIDENTS",
      "COMMAND ESTABLISHED REF TRAFFIC ACCIDENT (ENTRAPMENT)",
      "COMMAND ESTABLISHED SMOKE IN STRUCTURE COMMERICAL",
      "DIABETIC PROBLEMS",
      "EMD2",
      "EMDA",
      "EMDB",
      "EMDC",
      "EMDD",
      "FALLS - NOT ALERT",
      "FALLS",
      "FIRE OUTSIDE STRUCTURE",
      "FUEL ODOR ONLY OUTSIDE",
      "GAS EXPLOSION",
      "GAS LEAK INSIDE NATURAL",
      "GAS LEAK OUTSIDE NATURAL",
      "GAS LEAK OUTSIDE PROPANE",
      "GAS LEAK/ODOR INSIDE",
      "GAS LEAK/ODOR INSIDE W/INJ",
      "GAS LEAK/ODOR OUTSIDE",
      "GAS ODOR INSIDE ODOR ONLY",
      "GENERAL ALERT",
      "HAZARDOUS CONDITION",
      "HEADACHE",
      "HEART PROBLEMS",
      "HIGH ANGLE RES",
      "HIGH ANGLE RESCUE",
      "ILLEGAL BURN",
      "MECHANICAL ALARM COMMERCIAL",
      "MECHANICAL ALARM RESIDENTIAL",
      "MISCELLANEOUS",
      "MVC",
      "MVC (ENTRAPMENT)",
      "MVC (VEH VS BLDG)",
      "MVC ENTRAPMENT",
      "MVC FUEL/FLUID LEAK",
      "MVC NO INJ NO HAZ",
      "NATURAL / PROPANE GAS LEAK INSIDE",
      "NATURAL / PROPANE GAS LEAK OUTSIDE",
      "NATURAL GAS LEAK INSIDE",
      "OUTSIDE FIRE",
      "OVEN FIRE (CONTAINED)",
      "OVERDOSE / POISONING - OVERRIDE",
      "OVERDOSE / POISONING",
      "PREGNANCY / CHILDBIRTH",
      "PSYCHIATRIC",
      "PUBLIC ASSIST",
      "SICK PERSON",
      "SICK PERSON-C",
      "SMOKE IN STRUCTURE COMMERCIAL",
      "SMOKE IN STRUCTURE RESIDENTIAL",
      "SMOKE ODOR COMM STRUC",
      "SMOKE ODOR IN STRUCTURE",
      "SMOKE ODOR IN STRUCTURE",
      "SMOKE ODOR RES STRUC",
      "STAB / GUNSHOT / PENETRATING TRAUMA - OVERRIDE-GUNSHOT",
      "STAB / GUNSHOT / PENETRATING TRAUMA",
      "STAB / GUNSHOT",
      "STRUCT FIRE COMM TRAPPED",
      "STRUCT FIRE COMM",
      "STRUCT FIRE RES",
      "STRUCTURE FIRE COMMERCIAL",
      "STRUCTURE FIRE OUT",
      "STRUCTURE FIRE RESIDENTIAL",
      "TRAFFIC / TRANSPORTATION ACCIDENTS",
      "TRAFFIC ACCIDENT (ENTRAPMENT)",
      "TRAFFIC ACCIDENT (SUBMERGED)",
      "TRAFFIC ACCIDENT (VEH VS STRUCTURE)",
      "TRAFFIC ACCIDENT - PINNED (TRAPPED) VICTIM",
      "TRAFFIC ACCIDENT",
      "TRAUMATIC INJURIES",
      "UNCONSCIOUS / FAINTING",
      "UNCONSCIOUS/FAINTING",
      "UNKNOWN PROBLEM (PERSON DOWN)",
      "UNKNOWN PROBLEM",
      "UPDATE:  COMMAND TERMINATED - E16 ADVISED LOG IN FIREPLACE REF SMOKE IN STRUCTURE COMMERICAL",
      "VEHICLE FIRE",
      "WATER FLOW ALARM",
      "WATER RESCUE",
      "WORKING STRUCTURE FIRE RESIDENTIAL"
   );

  private static final String[] MW_STREET_LIST = new String[] {
    "BEAR LAKE",
    "BRANTLEY HALL",
    "BROOK HOLLOW",
    "CONTROL TOWER",
    "CORAL GLEN",
    "COTTONWOOD CREEK",
    "GOLDEN DAYS",
    "GOPHER SLOUGH",
    "GRASSY POINT",
    "HARBOR LIGHT",
    "HERITAGE PARK",
    "HIDDEN MEADOWS",
    "HISTORIC GOLDSBORO",
    "HITCHING POST",
    "HOWELL BRANCH",
    "ISLAND BAY",
    "KING EDWARD",
    "LAKE EMMA",
    "LAKE HARNEY WOODS",
    "LAKE HOWELL",
    "LAKE MARY",
    "LONGWOOD LAKE MARY",
    "MARKHAM WOODS",
    "MITCHELL HAMMOCK",
    "POINTE NEWPORT",
    "QUEENS MIRROR",
    "RED BIRD",
    "RED BUG LAKE",
    "REDWOOD GROVE",
    "REGAL POINTE",
    "ROSE MALLOW",
    "SABAL LAKE",
    "SABAL PARK",
    "SEVILLE CHASE",
    "SHADY HOLW", //the weird spelling is consistent even between departments
    "SPANISH TRACE",
    "ST JOHNS",
    "TRIPLET LAKE",
    "VILLAGE OAK",
    "WILLA SPRINGS",
    "WINDING CHASE",
    "WINDING HOLLOW",
    "WINDING LAKE",
    "WINDSOR CRESCENT",
    "WINTER GREEN",
    "WINTER PARK"

  };

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "ALTAMONTE SPRINGS",
      "CASSELBERRY",
      "LAKE MARY",
      "LONGWOOD",
      "OVIEDO",
      "SANFORD",
      "WINTER SPRINGS",

      // Census-designated places
      "BLACK HAMMOCK",
      "CHULUOTA",
      "FERN PARK",
      "FOREST CITY",
      "GENEVA",
      "GOLDENROD",
      "HEATHROW",
      "MIDWAY",
      "WEKIWA SPRINGS",

      // Unincorporated communities
      "BERTHA",
      "INDIAN MOUND VILLAGE",
      "LAKE MONROE",
      "SLAVIA",
      "SANLANDO SPRINGS",
      "TAINTSVILLE",
      "TUSKAWILLA",

      // Former communities
      "MARKHAM",
      "OSCEOLA",
      "GOLDSBORO",

      // Brevard County
      "MIMS",

      // Orange County
      "APOPKA",
      "MAITLAND",
      "ORLANDO",
      "WINTER PARK"

  };
}
