package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchGlobalDispatchParser;



public class MOOsageCountyAParser extends DispatchGlobalDispatchParser {

  public MOOsageCountyAParser() {
    super(CITY_LIST, "OSAGE COUNTY", "MO", PLACE_FOLLOWS_CALL | PLACE_FOLLOWS_ADDR);
    setupCities(CITY_CODES);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "CEDAR CREEK",
        "CHURCH HILL",
        "CRIPPLE CREEK",
        "HONEY CREEK",
        "LINN MEADOWS",
        "MARTINS BLUFF",
        "ROLLINS FERRY",
        "TOWER RIDGE",
        "TWIN RIDGE"
    );
    setAllowDirectionHwyNames();
  }

  @Override
  public String getFilter() {
    return "sms911@socket.net,osage911sms@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DASH_DIR_PTN = Pattern.compile("- *([NSEW]|[NS][EW])\\b");
  private static final Pattern STATE_PLACE_PTN = Pattern.compile("(MO)\\b *(.*)");
  private static final Pattern CROSS_UNIT_PTN = Pattern.compile("(.* (?:[NSEW]|[NS][EW]))\\b *(.*)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    subject = subject.trim();
    if (!subject.equals("OsageCo 911 EOC") &&
        !subject.equals("OsageCounty 911/EOC") &&
        !subject.equals("Osage 911/EOC")) return false;

    body = DASH_DIR_PTN.matcher(body).replaceAll("$1");

    if (!super.parseMsg(body, data)) return false;

    data.strCity = convertCodes(data.strCity, CITY_CODES);

    // State usually ends up in place field
    Matcher match = STATE_PLACE_PTN.matcher(data.strPlace);
    if (match.matches()) {
      data.strState = match.group(1);
      data.strPlace = match.group(2);
    }

    // Split off unit info from cross street
    match = CROSS_UNIT_PTN.matcher(data.strCross);
    String sUnit;
    if (match.matches()) {
      data.strCross = match.group(1);
      sUnit = match.group(2);
    } else {
      sUnit = data.strCross;
      data.strCross = "";
    }
    data.strUnit = append(data.strUnit, " ", sUnit);

    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST") + " UNIT";
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN / PROBLEMS",
      "ALARM SOUNDING - FIRE (BUSINESS)",
      "ALARM SOUNDING - FIRE (RESIDENTIAL)",
      "ALARM SOUNDING - MEDICAL",
      "ANIMAL BITES / ATTACKS",
      "ASSIST ANOTHER AGENCY - EMS",
      "ASSIST ANOTHER AGENCY - FIRE",
      "BACK PAIN",
      "BREATHING PROBLEMS",
      "BRUSH FIRE",
      "BURGLARY",
      "CARDIAC OR RESPIRATORY ARREST / DEATH",
      "CHEST PAIN /  CHEST DISCOMFORT",
      "CHOKING",
      "CONVULSIONS / SEIZURES",
      "DIABETIC PROBLEMS",
      "FALLS",
      "FLU FIRE",
      "FUEL SPILL / GAS LEAK",
      "HEART PROBLEMS / A.I.C.D",
      "HEMORRHAGE / LACERATION",
      "INVESTIGATION - FIRE",
      "LANE BLOCKAGE",
      "LIFT ASSIST",
      "MISSING PERSON / SEARCH & RESCUE",
      "MOVE UP EMS",
      "POWER LINES DOWN",
      "PREGNANCY / CHILDBIRTH / MISCARRIAGE",
      "PSYCHIATRIC / SUICIDAL ATTEMPT",
      "RESPONDR INFORMATION",
      "SICK PERSON",
      "STAB / GUNSHOT / PENETRATING TRAUMA",
      "STRANDED MOTORIST",
      "STROKE / TRANSIENT ISCHEMIC ATTACK",
      "STRUCTURE FIRE",
      "SUSPICIOUS ODOR",
      "TEST MESSAGE",
      "TRANSFER / INTERFACILITY/PALLIATIVE CARE",
      "TRANSFER - MEDICAL",
      "TRAUMATIC EMERGENCIES",
      "UNCONSCIOUS / FAINTING",
      "VEHICLE / ATV ACCIDENT - NON-INJURY",
      "VEHICLE / ATV ACCIDENT - UNKNOW INJURIES",
      "VEHICLE / ATV ACCIDENT - WITH INJURIES",
      "VEHICLE FIRE",
      "WALK IN - MEDICAL"
  );

  private static final String[] CITY_LIST = new String[]{
    "ARGYLE",
    "BELLE",
    "BONNOTS MILL",
    "CHAMOIS",
    "FOLK",
    "FRANKENSTEIN",
    "FREEBURG",
    "KOELTZTOWN",
    "LINN",
    "LOOSE CREEK",
    "META",
    "RICH FOUNTAIN",
    "WESTPHALIA",

    "COLE COUNTY",
    "ST THOMAS",

    "GASCONADE COUNTY",
    "MORRISON",

    "MARIES COUNTY",
    "VIENNA",

    "MILLER COUNTY",
    "ST ELIZABETH",
    "ST ELEZABETH",   // Dispatch typo

    "OSAGE COUNTY"
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ST ELEZABETH",     "ST ELIZABETH"
  });
}
