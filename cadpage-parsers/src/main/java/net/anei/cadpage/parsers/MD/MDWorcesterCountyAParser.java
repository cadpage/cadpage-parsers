package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class MDWorcesterCountyAParser extends DispatchOSSIParser {

  public MDWorcesterCountyAParser() {
    super(MDWorcesterCountyParser.CITY_LIST, "WORCESTER COUNTY", "MD",
    		   "FYI? SRC? CH? CALL ( CITY ADDR! DIR? APT? | ADDR! DIR? APT? PLACE+? CITY/Y ) ( DATETIME | X+? INFO+? DATETIME )");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("Text Message")) body = body.replace(subject, "");
    body = body.replace('\n', ' ');
    if (!super.parseMsg(body, data)) return false;
    MDWorcesterCountyParser.fixCity(data);
    String unit = CALL_UNIT_TABLE.getProperty(data.strCall);
    if (unit != null) data.strUnit = unit;
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CALL UNIT").replace("CITY", "CITY ST");
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[0-9]{1,2}00[A-Z]?|S[0-9]", true);
    if (name.equals("CH")) return new ChannelField("[A-Z]{1,2}", true);
    if (name.equals("DIR")) return new MyDirField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("PLACE")) return new  MyPlaceField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d");
    return super.getField(name);
  }

  private class MyDirField extends Field {
    public MyDirField() {
      setPattern("[NSEW](?:/?B)?", true);
    }

    @Override
    public void parse(String field, Data data) {
      field = field.replace("/", "");
      data.strAddress = append(data.strAddress, " ", field);
    }

    @Override
    public String getFieldNames() {
      // TODO Auto-generated method stub
      return null;
    }

  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM) *(.*)|(BLDG *.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (!match.matches()) return false;
      String apt = match.group(1);
      if (apt == null) apt = match.group(2);
      data.strApt = apt;
      return true;
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {

      // Occasionally they have an apt field that does not look like
      // an apt field and was mistaken for a place field.  When the
      // real place field comes along, move the old place value to
      // the apt field.
      if (data.strPlace.length() > 0) {
        if (data.strApt.length() > 0) abort();
        data.strApt = data.strPlace;
        data.strPlace = "";
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private static final Properties CALL_UNIT_TABLE = buildCodeTable(new String[]{
      "ABDOMINAL PAIN PROBLEMS",        "MEDICAL",
      "AIRCRAFT STANDBY",               "PUBLIC_SERVICE_CALLS",
      "ALLERGIC REACTIONS",             "MEDICAL",
      "ANIMAL BITES ATTACKS",           "MEDICAL",
      "ASSAULT SEXUAL ASSAULT",         "MEDICAL",
      "AST FM EOD W/ EXPLOSIVE DEV",    "PUBLIC_SERVICE_CALLS",
      "BACK PAIN",                      "MEDICAL",
      "BOAT FIRE",                      "FIRE_ALARM",
      "BREATHING PROBLEMS",             "MEDICAL",
      "BRUSH FIRE",                     "AUTO",
      "BUILDING FIRE",                  "FIRE_ALARM",
      "BURNS EXPLOSION",                "AUTO",
      "CARBON MONOXIDE DET ACTIVATION", "AUTO",
      "CARBON MONOXIDE INHALATION HA",  "AUTO",
      "CARDIAC OR RESPIRATORY ARREST",  "CPR",
      "CHEST PAIN",                     "MEDICAL",
      "CHOKING",                        "CPR",
      "CONVULSIONS SEIZURES",           "MEDICAL",
      "DIABETIC PROBLEMS",              "MEDICAL",
      "DROWNING DIVING SCUBA ACCIDEN",  "WATER_RESCUE",
      "ECD DEPLOYMENT",                 "MEDICAL",
      "ELECTRICAL HAZARD",              "AUTO",
      "ELECTROCUTION LIGHTNING",        "AUTO",
      "ELEV RESCUE WITHOUT",            "MEDICAL",
      "EXPLOSION REPORT OF AN EXPLOS",  "FIRE_ALARM",
      "EYE PROBLEMS INJURIES",          "MEDICAL",
      "FALLS",                          "MEDICAL",
      "FIRE ALARM ACTIVATION",          "AUTO",
      "GAS LEAK",                       "AUTO",
      "HAZMAT INCIDENT",                "AUTO",
      "HEADACHE",                       "MEDICAL",
      "HEART PROBLEMS AICD",            "MEDICAL",
      "HEAT COLD EXPOSURE",             "MEDICAL",
      "HEMORRHAGE",                     "MEDICAL",
      "HOUSE FIRE",                     "FIRE_ALARM",
      "INACCESSIBLE OTHR ENTRAPMENTS",  "AUTO",
      "INVESTIGATE SMOKE ODOR ETC",     "AUTO",
      "LIQUID SPILL",                   "PUBLIC_SERVICE_CALLS",
      "NON EMERGENCY TRANSPORT BY EMS", "MEDICAL",
      "OVERDOSE POISONING",             "MEDICAL",
      "PREGNANCY CHILDBIR MISCARRIAG",  "MEDICAL",
      "PRESUMED DEAD ON ARRIVAL",       "MEDICAL",
      "PSYCHIATRIC ABNORMAL BEHAVIOR",  "MEDICAL",
      "PUBLIC SERVICE EMS",             "MEDICAL",
      "PUBLIC SERVICE FIRE",            "PUBLIC_SERVICE_CALLS",
      "SAM BOX ACTIVATION",             "AUTO",
      "SICK PERSON",                    "MEDICAL",
      "SOMEONE LOCKED IN OUT RES VEH",  "PUBLIC_SERVICE_CALLS",
      "STAB GUNSHOT PENETRATING TRAU",  "MEDICAL",
      "STANDBY EMS OR FIRE CREW",       "MEDICAL",
      "STROKE CVA",                     "MEDICAL",
      "TRAFFIC TRANSPORTATION ACCDTS",  "AUTO",
      "TRAINING DRILL FOR FIRE DEPT",   "PUBLIC_SERVICE_CALLS",
      "TRANSFER INTERFACILITY",         "MEDICAL",
      "TRASH FIRE W OR W/O EXPOSURE",   "AUTO",
      "TRAUMATIC INJURIES",             "MEDICAL",
      "UNCONSCIOUS FAINTING",           "MEDICAL",
      "UNKNOWN PROBLEM MAN DOWN",       "MEDICAL",
      "UNKNOWN TYPE FIRE",              "AUTO",
      "UTILITY POLE FIRE",              "AUTO",
      "VEHICLE FIRE",                   "AUTO",
      "WATER/WASTEWATER EMERGENCY",     "AUTO"
  });
}
