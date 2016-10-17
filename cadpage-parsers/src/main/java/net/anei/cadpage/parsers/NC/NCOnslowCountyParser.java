package net.anei.cadpage.parsers.NC;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.TestCodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCOnslowCountyParser extends DispatchOSSIParser {
  
  private String selectValue;
  
  public NCOnslowCountyParser() {
    super(CITY_CODES, "ONSLOW COUNTY", "NC",
           "( CALL ADDR! CITY DIST? INFO+ | ADDR APT? ( SELECT/EMS PLACE+? UNIT CALL! CODE | SELECT/FIRE PLACE+? CALL/Z SRC! UNIT | CITY? PLACE+? CALL/Z END ) )");
  }
  
  @Override
  public String getFilter() {
    return "CAD@onslowcountync.gov";
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
    return (data.strCall.length() > 0 || isPositiveId() && data.strCity.length() > 0);
  }

  @Override
  protected String getSelectValue() {
    return selectValue;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DIST")) return new PlaceField("DIST:.*");
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("CODE")) return new MyCodeField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (selectValue.equals("CALL") || CALL_LIST.contains(field)) {
        super.parse(field, data);
        return true;
      }
      return false;
    }
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:LOT|APT|UNIT|SUITE) *(.*)|\\d+");
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
  
  private static final Pattern SOURCE_PTN = Pattern.compile("[A-Z]{1,2}FD|OCRS");
  private class MySourceField extends SourceField {
    public MySourceField() {
      setPattern(SOURCE_PTN, true);
    }
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("\\d{1,2}[A-Z]?|[A-Z]{2}FD");
  private class MyUnitField extends UnitField {
    public MyUnitField() {
      setPattern(UNIT_PTN, true);
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, "-", field);
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
      "ALARMS",
      "ALARMS 7700",
      "ALLERGIES/ENVENOMATIONS",
      "ANIMAL BITES/ATTACKS",
      "ARSON 2000",
      "ASSAULT/SEXUAL ASSALUT",
      "ASSAULT/ SEXUAL ASSAULT",
      "ASSIST CITIZEN 8100",
      "ASSIST OTHER JURISD 7600",
      "BACK PAIN",
      "BREATHING PROBLEMS",
      "BURNS EXPLOSION",
      "CARBON MONOXIDE INHALATION HAT",
      "CARDIAC ARREST DEATH",
      "CHEST PAIN",
      "CITIZEN ASSIST/SERVICE CALL",
      "CONVULSIONS/SEIZURES",
      "DEATH/INJURY",
      "DIABETIC PROBLEMS",
      "DOMESTIC DISTURBANCE/ VIOLENCE",
      "DRIVING UNDER THE INFLUENCE",
      "DROWNING/DIVING/SCUBA ACCIDENT",
      "ELECTRICAL HAZARD",
      "ELECTROCUTION AND LIGHTNING",
      "ELEVATOR /ESCUALTOR RESCUE",
      "EMS STAND BY CALLS",
      "EXTRICATION/ENTRAPPED",
      "FALL",
      "FUEL SPILL",
      "GAS LEAK / GAS ODOR",
      "HEADACHE",
      "HEART PROBLEM",
      "HEMORRHAGE",
      "INACCESSIBLE INCIDENT",
      "LIGHTENING STRIKE",
      "MOTOR VEHICLE COLLISION",
      "MUTUAL AID",
      "ODOR",
      "OUTSIDE FIRE",
      "OVERDOSE/POISONING",
      "PREGNANCY",
      "PSYCHIATRIC/ABNORMAL BEHAV",
      "SICK PERSON",
      "SMOKE INVESTIGATION",
      "STAB/GUNSHOT/PEN TRAUMA",
      "STRUCTURE FIRE",
      "STROKE CVA",
      "SUSPICIOUS/WANTED (PERSON VEH)",
      "TRAFFIC",
      "TRAFFIC ACCIDENT 5500",
      "TRAFFIC STOP",
      "TRAFFIC TRANSPORTATION ACCIDT",
      "TRAFFIC/ TRANSPORTATION CRASH",
      "TRAUMATIC INJURIES",
      "UNCONSCIOUS FAINTING",
      "UNKNOWN PROBLEM MAN DOWN",
      "VEHICLE FIRE",
      "WALK-UP",
      "WATERCRAFT IN DISTRESS"
  }));
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "JAX",  "JACKSONVILLE",
      "HR",   "HOLLY RIDGE",
      "HUB",  "HUBERT",
      "MAY",  "MAYSVILLE",
      "RICH", "RICHLANDS",
      
      // Carteret County
      "CEDAR POINT",    "CEDAR POINT"
  });
}
