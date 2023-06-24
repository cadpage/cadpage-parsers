
package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ORMarionCountyBParser extends DispatchH05Parser {

  public ORMarionCountyBParser() {
    this("MARION COUNTY", "OR");
  }

  protected ORMarionCountyBParser(String defCity, String defState) {
    super(defCity, defState,
          "SRC_CODE DATETIME ADDRCITY UNIT ID INFO_BLK/Z+? GPS1 GPS2 END");
  }

  @Override
  public String getAliasCode() {
    return "ORMarionCountyB";
  }

  @Override
  public String getFilter() {
    return "WVCChelpdesk@cityofsalem.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_CR;
  }

  private static final Pattern CODE_PTN = Pattern.compile("([A-Z]+)(\\d?)");

  @Override
  protected boolean parseFields(String[] flds, Data data) {
    for (int jj = 0; jj < flds.length; jj++) {
      flds[jj] = stripFieldStart(stripFieldEnd(flds[jj], ";"), ";");
    }
    if (!super.parseFields(flds, data)) return false;

    Matcher match = CODE_PTN.matcher(data.strCode);
    if (match.matches()) {
      String call = CALL_CODES.getProperty(match.group(1));
      if (call != null) {
        data.strCall = append(call, " ", match.group(2));
      }
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC_CODE")) return new MySourceCodeField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private class MySourceCodeField extends Field {

    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(';');
      if (pt >= 0) {
        data.strSource = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      data.strCode = field;
    }

    @Override
    public String getFieldNames() {
      return "SRC CODE";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[] {
      "ABDOM",  "Abdominal Pain",
      "ACC",    "Motor vehicle accident w/injury",
      "ACCHR",  "Hit and run accident w/injury",
      "AIREM",  "Air Emergency via phone",
      "ALERT",  "Air Emergency via tower",
      "ALREAC", "Allergic Reaction",
      "ALRMCO", "Carbon monoxide alarm",
      "ALRMF",  "Fire alarm",
      "ALRMMED","Medical alarm",
      "AOAP",   "Assist Police",
      "APT",    "Apartment fire",
      "ASLT",   "Assault with injury",
      "BACK",   "Back pain",
      "BITE",   "Animal Bite",
      "BLEED",  "Bleeding problem",
      "BOAT",   "Boat accident",
      "BOMBF",  "Bomb device",
      "BREATH", "Difficulty breathing",
      "BURN",   "Burn injury",
      "BURNCO", "Burning complaint",
      "BURNIN", "Daily Burning information",
      "CAR",    "Car fire",
      "CHEST",  "Chest pain",
      "CHIM",   "Chimney/flue fire",
      "CHOKE",  "Choking",
      "COLD",   "Cold fire investigation",
      "COMML",  "Commercial fire",
      "DIAB",   "Diabetic issue",
      "DOMDIS", "Domestic disturbanc w/injury",
      "DROWN",  "Drowning",
      "EXPLOD", "Explosion",
      "EXPOSE", "Cold or heat exposure",
      "EYE",    "Eye injury",
      "FALL",   "Fall",
      "FIGHT",  "Fight with injury",
      "FIRENH", "Non-structure high risk fire",
      "FIRENL", "Non-structure low risk fire",
      "FIRES",  "Structure fire",
      "GRASSH", "High risk grass fire",
      "GRASSL", "Low risk grass fire",
      "GSW",    "Gun shot wound",
      "HA",     "Headache",
      "HAZBDS", "Biological Detection System (Salem Post Office)",
      "HAZMAT", "Hazardous Materials Incident",
      "HOUSE",  "House fire",
      "INDUST", "Industrial accident",
      "LAB",    "Drug lab standby",
      "MARINE", "Boat/marine fire",
      "MCI",    "Mass casualty incident",
      "MEDVAC", "Air ambulance transport",
      "MENTAL", "Mental subject w/injury",
      "MPI",    "Multi patient incident",
      "MUTUAL", "Mutual Aid",
      "NATGAS", "Natural gas leak",
      "OB",     "Obstetric Distress or Childbirth",
      "OD",     "Overdose",
      "ODORNS", "Odor of Smoke - not seen (in or outside)",
      "OTTRAN", "Out of town transport",
      "POISON", "Poisoning",
      "PUBLIC", "Public Assist",
      "RESCUE", "Land based rescue",
      "ROB",    "Robbery w/injury",
      "SEXOFF", "Sex offense w/injury",
      "SIREN",  "TEST Tsunami Siren",
      "SMOKEN", "Visable smoke non-structure",
      "SMOKES", "Visable smoke structure",
      "SPILL",  "Fuel spill or leak",
      "STAB",   "Stabbing",
      "STNDBY", "Aircraft standby",
      "STROKE", "Stroke/CVA",
      "SURF",   "Surf Rescue",
      "TESTAIR","Training - Aircraft Emergency",
      "TESTF",  "Test or information - Fire",
      "TESTM",  "Test or information - Fire",
      "TIME",   "Transfer from health care facility",
      "TOXIC",  "Toxic exposure",
      "TRAIN",  "Train derailment w/injury",
      "TRANS",  "Medical transfer",
      "TRAP",   "Accident with entrapment",
      "TRAUMA", "Bicycle crash/trauma/fracture",
      "TREE",   "Tree fire",
      "UNCON",  "Unconscous person",
      "UNKMED", "Unknown medical",
      "WATER",  "Water rescue",
      "WCTRAN", "Wheelchair transport",
      "WIRE",   "Wire down"

  });
}
