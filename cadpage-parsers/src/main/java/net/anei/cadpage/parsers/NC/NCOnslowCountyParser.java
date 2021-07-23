package net.anei.cadpage.parsers.NC;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.TestCodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCOnslowCountyParser extends DispatchOSSIParser {

  private String selectValue;

  public NCOnslowCountyParser() {
    super(CITY_CODES, "ONSLOW COUNTY", "NC",
           "FYI? ( UNIT_CH ADDR! CITY DIST? INFO/N+ " +
                "| ID? ADDR APT? ( PLACE CITY | CITY | ) APT? DIST? EMPTY+? ( CALL | PLACE CALL! | CALL ) INFO/N+? SRC EMPTY? UNIT END " +
                ")");
  }

  @Override
  public String getFilter() {
    return "CAD@onslowcountync.gov";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public String adjustMapAddress(String address) {
    address = MAIN_ST_EX.matcher(address).replaceAll("MAIN ST EXT");
    address = WILLIAMS_ST_EX.matcher(address).replaceAll("WILLIAMS ST EXD");
    return address;
  }
  private static final Pattern MAIN_ST_EX = Pattern.compile("\\bMAIN ST EX\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern WILLIAMS_ST_EX = Pattern.compile("\\bWILLIAMS ST EX\\b", Pattern.CASE_INSENSITIVE);


  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0, pt).trim();
    body = MSPACE_PTN.matcher(body).replaceAll(" ");
    body = stripFieldStart(body,  "/ no subject /");
    selectValue = (body.contains("[") ? "CALL" : null);
    return super.parseMsg(body, data);
  }

  @Override
  protected boolean parseFields(String[] fields, Data data) {
    if (fields.length == 0) return false;
    if (selectValue == null) {
      String lastField = fields[fields.length-1];
      if (CODE_PTN.matcher(lastField).matches()) {
        selectValue = "EMS";
      } else if (UNIT_PTN.matcher(lastField).matches() ||
                 SOURCE_PTN.matcher(lastField).matches()) {
        selectValue = "FIRE";
      } else {
        selectValue = "OTHER";
      }
    }
    if (!super.parseFields(fields, data)) return false;

    // Call description really should be a required field.  But it is occasionally missing
    // so we will accept calls without it if they have been positively ID's
    return (data.strCall.length() > 0 || isPositiveId() && data.strAddress.length() > 0);
  }

  @Override
  protected String getSelectValue() {
    return selectValue;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("UNIT_CH"))  return new MyUnitChannelField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DIST")) return new PlaceField("DIST:.*", true);
    if (name.equals("ID")) return new IdField("\\d+", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("CODE")) return new MyCodeField();
    return super.getField(name);
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*) (\\d{1,3}-?[A-Z]-?\\d{1,2}-?[A-Za-z]?|\\d{4})");
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCall = match.group(1).trim();
        data.strCode = match.group(2);
        return true;
      }
      if (CALL_LIST.contains(field)) {
        data.strCall = field;
        return true;
      }
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCode = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE?";
    }
  }

  private static final Pattern UNIT_CHANNEL_PTN = Pattern.compile("(?:\\{([A-Z0-9]+)\\} )?EVENT CHANNEL (\\d+)");
  private class MyUnitChannelField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = UNIT_CHANNEL_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strUnit = getOptGroup(match.group(1));
      data.strChannel = match.group(2);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT CH";
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (CODE_PTN.matcher(field).matches()) {
        data.strCode = field;
      }
      else if (UNIT_PTN.matcher(field).matches()) {
        data.strUnit = append(data.strUnit, ",", field);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE UNIT?";
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:LO?T|APT|UNIT|SUITE) *(.*)|\\d+|H-?\\d{1,2}|[A-D]");
  private class MyAptField extends AptField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // See if we match the master apartment pattern
      Matcher match = APT_PTN.matcher(field);
      if (!match.matches()) return false;

      // Match succeeded, but there are two possible outcomes.  If this had
      // some kind of apartment prefix, take it as gospel
      String apt = match.group(1);
      if (apt != null) {
        data.strApt = apt;
        return true;
      }

      // If we are processing the EMS format, we have to take some additional
      // steps to distinguish apt fields from unit fields.  if not, then this is
      // an APT field

      if (!selectValue.equals("EMS")) {
        data.strApt = field;
        return true;
      }

      // The EMS format has a unit field coming up that looks a lot like an apt field.
      // The only way we can determine if this is an apartment
      // field is by looking at the next two fields to see if one of them looks
      // like a unit field.  If not, then we must be the unit field
      boolean found = false;
      for (int ndx = 1; ndx <= 2; ndx++) {
        String fld = getRelativeField(ndx);
        if (UNIT_PTN.matcher(fld).matches()) {
          found = true;
          break;
        }
      }
      if (!found) return false;
      data.strApt = field;
      return true;
    }
  }

  private static final Pattern SOURCE_PTN = Pattern.compile("[A-Z]{1,2}FD|OCRS|WCF2");
  private class MySourceField extends SourceField {
    public MySourceField() {
      setPattern(SOURCE_PTN, true);
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("\\d{1,2}[A-Z]?|[A-Z]{2}FD|WCF2");
  private class MyUnitField extends UnitField {
    public MyUnitField() {
      setPattern(UNIT_PTN, true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, ",", field);
    }
  }

  private static final Pattern CODE_PTN = Pattern.compile("\\d{1,3}[A-Z]\\d{1,2}[A-Za-z]?");
  private class MyCodeField extends CodeField {
    public MyCodeField() {
      setPattern(CODE_PTN, true);
    }
  }

  @Override
  public CodeSet getCallList() {
    return new TestCodeSet(CALL_LIST);
  }

  private static final Set<String> CALL_LIST = new HashSet<String>(Arrays.asList(new String[]{
      "ABDOMINAL PAIN",
      "AIRCRAFT EMERGENCY",
      "ALARMS",
      "ALARMS PD",
      "ALLERGIES/ENVENOMATIO",
      "ALLERGIES/ENVENOMATIONS",
      "ANIMAL",
      "ANIMAL BITES/ATTACKS",
      "ARSON",
      "ASSAULT/SEXUAL ASSALUT",
      "ASSAULT/SEXUAL ASSAUL",
      "ASSAULT/SEXUAL ASSAULT",
      "ASSAULT/ SEXUAL ASSAULT",
      "ASSIST CITIZEN",
      "ASSIST OTHER AGENCIES",
      "ASSIST OTHER JURISD",
      "BACK PAIN",
      "BREATHING PROBLEMS",
      "BURNS EXPLOSION",
      "CANCEL",
      "CARBON MONOXIDE INHAL",
      "CARBON MONOXIDE INHALATION HAT",
      "CARDIAC ARREST DEATH",
      "CHEST PAIN",
      "CHOKING",
      "CITIZEN ASSIST/SERVICE CALL",
      "CONVULSIONS/SEIZURES",
      "DEATH/INJURY",
      "DIABETIC PROBLEMS",
      "DISTURBANCE/ NUISANCE",
      "DOMESTIC DISTURBANCE/ VIOLENCE",
      "DRIVING UNDER THE INFLUENCE",
      "DROWNING/DIVING/SCUBA ACCIDENT",
      "DRUGS",
      "ELECTRICAL HAZARD",
      "ELECTROCUTION AND LIGHTNING",
      "ELEVATOR /ESCUALTOR RESCUE",
      "EMS STAND BY CALLS",
      "EXPLOSION",
      "EXTRICATION/ENTRAPPED",
      "FALL",
      "FUEL SPILL",
      "GAS LEAK / GAS ODOR",
      "GRASS FIRE",
      "HAZMAT",
      "HEADACHE",
      "HEART PROBLEM",
      "HEMORRHAGE",
      "INACCESSIBLE INCIDENT",
      "INCIDENT/SERVICES",
      "LANDING ZONE",
      "LIGHTENING STRIKE",
      "LIGHTNING STRIKE",
      "MARINE FIRE",
      "MENTAL DISORDER (BEHAVIORAL)",
      "MISCELLANEOUS",
      "MOTOR VEHICLE COLLISION",
      "MUTUAL AID",
      "ODOR",
      "OUTSIDE FIRE",
      "OVERDOSE/POISONING",
      "PANDEMIC/EPIDEMIC/OUTBREAK",
      "PREGNANCY",
      "PSYCHIATRIC/ABNORMAL BEHAV",
      "SICK PERSON",
      "SINKING VEH/VEH IN FLOODWATER",
      "SMOKE INVESTIGATION",
      "STAB/GUNSHOT/PEN TRAUMA",
      "STAB/GUNSHOT/PEN TRAUMA CENTR",
      "STAB/GUNSHOT/PEN TRAUMA UNCON",
      "STRUCTURE FIRE",
      "STROKE CVA",
      "SUICIDAL PERSON/ ATTEMPTED",
      "SUSPICIOUS/WANTED (PERSON VEH)",
      "TRAFFIC",
      "TRAFFIC / TRANSPORTATION",
      "TRAFFIC/ TRANSPORTATION CRASH",
      "TRAFFIC ACCIDENT",
      "TRAFFIC STOP",
      "TRAFFIC TRANS ACCIDT",
      "TRAFFIC TRANSPORTATION ACCIDT",
      "TRAFFIC VIOLATION/ COMPLAINT",
      "TRANSFER INTERFACILITY",
      "TRAUMATIC INJURIES",
      "TRESPASSING/ UNWANTED",
      "UNCONSCIOUS FAINTING",
      "UNKNOWN PROBLEM MAN DOWN",
      "UNKNOWN (3RD PARTY)",
      "VEHICLE FIRE",
      "WALK-UP",
      "WATER RESCUE",
      "WATERCRAFT IN DISTRESS"
  }));

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BAYB", "BAYBORO",
      "BEUL", "BEULAVILLE",
      "BUR",  "BURGAW",
      "CHIN", "CHINQUAPIN",
      "CL",   "CAMP LEJEUNE",
      "EI",   "EMERALD ISLE",
      "GOLD", "GOLDSBORO",
      "HAMO", "HAMPSTEAD",
      "HR",   "HOLLY RIDGE",
      "HUB",  "HUBERT",
      "JAX",  "JACKSONVILLE",
      "KEN",  "KENANSVILLE",
      "KIN",  "KINSTON",
      "MAY",  "MAYSVILLE",
      "MH",   "MAPLE HILL",
      "MOR",  "MOREHEAD CITY",
      "MP",   "MIDWAY PARK",
      "NEWB", "NEW BERN",
      "NP",   "NEWPORT",
      "NTB",  "NORTH TOPSAIL BEACH",
      "PINK", "PINK HILL",
      "POL",  "POLLOCKVILLE",
      "RICH", "RICHLANDS",
      "SC",   "SURF CITY",
      "SF",   "SNEADS FERRY",
      "SOU",  "SOUTHPORT",
      "STEL", "STELLA",
      "SWAN", "SWANSBORO",

      // Carteret County
      "CEDAR POINT",    "CEDAR POINT"
  });
}
