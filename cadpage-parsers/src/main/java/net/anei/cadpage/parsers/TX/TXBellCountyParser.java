package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
/**
 * Bell County, TX
 */
public class TXBellCountyParser extends FieldProgramParser {

  public TXBellCountyParser() {
    super(CITY_CODES, "BELL COUNTY", "TX",
        "( P:PRI! EVNUM:ID! LOC:ADDR1/S! ADDR2/S? APT:APT! LAT:GPS1! LONG:GPS2! TIME:DATETIME2! CALLR:NAME! CALLR_ADD:SKIP! CALLR_NUM:PHONE! UNITS:UNIT! EVENT_TYPE:CALL! Sub_Type:CALL/SDS! COMMENTS:INFO INFO/CS+ " +
        "| PRI1 ( LOC:ADDR/S APT:APT? MUN:CITY? | ) ( EVENT_TYPE:CODE! SubType:CODE! Comments:INFO Problem:INFO CALLER_NAME:NAME% CLRNUM:PHONE% TIME:TIME% EVNUM:ID | TYPE_CODE:CODE! SubType:CODE CALLER_NAME:NAME! CLRNUM:PHONE! TIME:TIME! Comments:INFO ) " +
        ")");
    setupGpsLookupTable(GPS_TABLE);
  }

  public String getFilter() {
    return "930010,28863700";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=Comments:|CALLER NAME:|CLRNUM:|TIME:)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("P:")) {
      if (body.length() >= 2980 && body.length() <= 3001) data.expectMore = true;
      return parseFields(body.split(","), data);
    }
    else {
      body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
      if (!super.parseMsg(body, data)) return false;
      String call = CALL_CODES.getProperty(data.strCode);
      if (call == null) {
        int pt = data.strCode.indexOf('-');
        if (pt >= 0) call = CALL_CODES.getProperty(data.strCode.substring(0,pt));
      }
      if (call == null) {
        data.strCall = append(data.strCall, " - ", data.strCode);
      } else {
        if (data.strPriority.isEmpty()) data.strPriority = call.substring(0,1);
        data.strCall = append(data.strCall, " - ", call.substring(2));
      }
      return true;
    }
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CODE", "CODE PRI CALL").replace("GPS", "GPS ADDR");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI1")) return new MyPriority1Field();
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("ADDR")) return new MyAddressField(0);
    if (name.equals("ADDR1")) return new MyAddressField(1);
    if (name.equals("ADDR2")) return new MyAddressField(2);
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("DATETIME2")) return new MyDateTime2Field();
    return super.getField(name);
  }

  private static final Pattern PRI1_PTN = Pattern.compile("(.*?) *\\bP(\\d)");
  private class MyPriority1Field extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = PRI1_PTN.matcher(field);
      if (!match.matches()) abort();
      String prefix = match.group(1);
      data.strPriority = match.group(2);

      int pt = prefix.indexOf("Original message from");
      if (pt >= 0) {
        if (!prefix.startsWith("EVENT:")) {
          data.strCall = prefix.substring(0, pt).trim();
        }
        pt = prefix.lastIndexOf(':');
        if (pt < 0) abort();
        data.strCall = append(data.strCall, " - ", prefix.substring(pt+1).trim());
      } else {
        data.strCall = prefix;
      }
    }

    @Override
    public String getFieldNames() {
      return "PRI? UNIT";
    }

  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*), *([^ ]+)(?: APT)?");
  private class MyAddressField extends AddressField {

    private int type;

    public MyAddressField(int type) {
      this.type = type;
    }

    @Override
    public boolean canFail() {
      return type == 2;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (type == 2 && !data.strAddress.startsWith("LL(")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (type == 2) {
        field = data.strAddress + ',' + field;
        data.strAddress = "";
      }
      field = field.replace("CHAPPARAL", "CHAPARRAL");
      if (field.startsWith("@")) {
        data.strAddress = field;
        return;
      }
      if (field.startsWith("LL(")) {
        if (type == 1) {
          data.strAddress = field;
          return;
        }
        int pt = field.indexOf(")", 3);
        if (pt < 0) abort();
        data.strAddress = field.substring(0,pt+1).trim();
        field = field.substring(pt+1);
      }
      for (String part : field.split(":")) {
        part = part.trim();
        if (data.strAddress.isEmpty()) {
          String apt = "";
          Matcher match = ADDR_APT_PTN.matcher(part);
          if (match.matches()) {
            part = match.group(1).trim();
            apt = match.group(2);
          }
          part = stripFieldEnd(part, " MUN");
          super.parse(part, data);
          data.strApt = append(data.strApt, "-", apt);
        }
        else if (part.startsWith("@")) {
          data.strPlace = append(data.strPlace, " - ", part.substring(1).trim());
        }
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strApt)) return;
      super.parse(field, data);
    }
  }

  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("default")) return;
      data.strCode = append(data.strCode, "-", field);
    }
  }

  private static final Pattern INFO_PHONE_GPS_PTN = Pattern.compile("(\\(\\d{3}\\) ?\\d{3}-\\d{4}|\\d{10}) +(?:TELCO=\\S* +)?([-+]\\d+\\.\\d+ [-+]\\d+\\.\\d+)(?: [-+]\\d+\\.\\d+ [-+]\\d+\\.\\d+)?(?: Location Saved by LocateCall - LL\\([-+\\d:\\.,]+?\\))?(?:: EST \\d+)?(?: WPH\\d)? *(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      for (String part : field.split("\\|")) {

        part = part.trim();

        Matcher match = INFO_PHONE_GPS_PTN.matcher(part);
        if (match.matches()) {
          if (data.strPhone.isEmpty()) data.strPhone = match.group(1);
          if (data.strGPSLoc.isEmpty()) setGPSLoc(match.group(2), data);
          part = match.group(3);
        }
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }

    @Override
    public String getFieldNames() {
      return "UNIT PHONE GPS INFO";
    }
  }

  private static final Pattern TIME_PTN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d)\\b *(.*)");

  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) {
        data.expectMore = true;
        return;
      }

      Matcher match = TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1), data);
      data.strCall = append(data.strCall, " - ", match.group(2));
    }
  }

  private static final Pattern DATE_TIME2_PTN = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2}) (\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTime2Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME2_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate =  match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(4);
    }
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    if (!address.startsWith("@")) return null;
    int pt = address.indexOf(':');
    if (pt >= 0) address = address.substring(0,pt);
    return address;
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "ADMIN",              "4/ADMIN DUTIES",
      "ADMIN-FD",           "4/ADMIN DUTIES - FD ADMIN DUITES",
      "AE",                 "2/ALLERGIES(REACTIONS)/ENVENOMATIONS (STINGS,BITES)",
      "AE-DB",              "2/ALLERGIES(REACTIONS)/ENVENOMATIONS (STINGS,BITES) - DIFFICULTY BREATHING/SWALLOWING",
      "AE-DS",              "1/ALLERGIES(REACTIONS)/ENVENOMATIONS (STINGS,BITES) - DIFFICULTY SPEAKING BETWEEN BREATHS",
      "AE-HIST",            "2/ALLERGIES(REACTIONS)/ENVENOMATIONS (STINGS,BITES) - HISTORY OF SEVERE ALLERGIC REACTION",
      "AE-IB",              "1/ALLERGIES(REACTIONS)/ENVENOMATIONS (STINGS,BITES) - INEFFECTIVE BREATHING",
      "AE-NA",              "1/ALLERGIES(REACTIONS)/ENVENOMATIONS (STINGS,BITES) - NOT ALERT",
      "AE-NDB",             "3/ALLERGIES(REACTIONS)/ENVENOMATIONS (STINGS,BITES) - NO DIFF BREATHING/SWALLOWING",
      "AE-SNAKE",           "1/ALLERGIES(REACTIONS)/ENVENOMATIONS (STINGS,BITES) - SNAKEBITE",
      "AE-SPIDER",          "3/ALLERGIES(REACTIONS)/ENVENOMATIONS (STINGS,BITES) - SPIDER BITE",
      "AE-SWARM",           "1/ALLERGIES(REACTIONS)/ENVENOMATIONS (STINGS,BITES) - SWARMING ATTACK",
      "AE-US",              "2/ALLERGIES(REACTIONS)/ENVENOMATIONS (STINGS,BITES) - UNKNOWN STATUS",
      "AL",                 "2/ALARM",
      "AL-FIRE",            "2/ALARM - FIRE ALARM",
      "AL-FIREAA",          "3/ALARM - FIRE ACCIDENTAL ACTIVATION",
      "ALERT1",             "1/AIRCRAFT EMERGENCY STANDBY",
      "ALERT2-FIRE",        "1/AIRCRAFT EMERGENCY - IN FLIGHT - FIRE",
      "ALERT2-MECH",        "1/AIRCRAFT EMERGENCY - IN FLIGHT - MECHANICAL",
      "ALERT2-THREAT",      "1/AIRCRAFT EMERGENCY - IN FLIGHT - TERRORIST THREAT/PERSON",
      "ALERT3-NOSTRUCT",    "1/AIRCRAFT CRASH OR FIRE - NO STRUCTURE",
      "ALERT3-STRUCT",      "1/AIRCRAFT CRASH OR FIRE - STRUCTURE",
      "ANIMAL",             "1/ANIMAL",
      "ANIMAL-ATK",         "1/ANIMAL - ATTACK OR MULTIPLE  ANIMALS",
      "ANIMAL-CN",          "1/ANIMAL - CHEST OR NECK INJURY (WITH DIFF BREATHING)",
      "ANIMAL-DAN",         "1/ANIMAL - DANGEROUS BODY AREA",
      "ANIMAL-EX",          "1/ANIMAL - EXOTIC ANIMAL",
      "ANIMAL-LG",          "1/ANIMAL - LARGE ANIMAL",
      "ANIMAL-NA",          "1/ANIMAL - NOT ALERT",
      "ANIMAL-ND",          "3/ANIMAL - NOT DANGEROUS BODY AREA",
      "ANIMAL-NR",          "3/ANIMAL - NON-RECENT (OVER 6 HRS) INJURIES (WITHOUT PRIORITY SYMPTOMS)",
      "ANIMAL-PD",          "2/ANIMAL - POSSIBLY DANGEROUS BODY AREA",
      "ANIMAL-SH",          "2/ANIMAL - SERIOUS HEMORRHAGE",
      "ANIMAL-SUP",         "3/ANIMAL - SUPERFICIAL BITES",
      "ANIMAL-UA",          "1/ANIMAL - UNCONSCIOUS OR ARREST",
      "ANIMAL-US",          "2/ANIMAL - UNKNOWN STATUS",
      "AP",                 "1/ABDOMINAL PAIN/PROBLEMS",
      "AP-AP",              "3/ABDOMINAL PAIN/PROBLEMS - ABDOMINAL PAIN/PROBLEMS",
      "AP-FAINT",           "2/ABDOMINAL PAIN/PROBLEMS - FAINTING OR NEAR FAINTING",
      "AP-FWF",             "2/ABDOMINAL PAIN/PROBLEMS - FEMALES W/ FAINTING OR NEAR FAINTING",
      "AP-FWPN",            "2/ABDOMINAL PAIN/PROBLEMS - FEMALES W/ PAIN ABOVE NAVEL",
      "AP-KAA",             "2/ABDOMINAL PAIN/PROBLEMS - KNOWN AORTIC ANEURYSM",
      "AP-MWPN",            "2/ABDOMINAL PAIN/PROBLEMS - MALES WITH PAIN ABOVE NAVEL",
      "AP-NA",              "1/ABDOMINAL PAIN/PROBLEMS - NOT ALERT",
      "AP-SAA",             "2/ABDOMINAL PAIN/PROBLEMS - SUSPECTED AORTIC ANEURYSM",
      "ARCING",             "1/TRANSFORMER ARCING",
      "ARCING-ARCING",      "1/TRANSFORMER ARCING - NO DOWNED LINE",
      "ARCING-LINE",        "1/TRANSFORMER ARCING - ELECTRICAL  LINE DOWN",
      "ASLT/SA",            "2/ASSAULT/SEXUAL ASSAULT",
      "ASLT/SA-ASLT-CN",    "1/ASSAULT/SEXUAL ASSAULT - CHEST OR NECK INJURY (WITH DIFF BREATHING)",
      "ASLT/SA-ASLT-MV",    "1/ASSAULT/SEXUAL ASSAULT - MULTIPLE VICTIMS",
      "ASLT/SA-ASLT-NA",    "1/ASSAULT/SEXUAL ASSAULT - NOT ALERT",
      "ASLT/SA-ASLT-ND",    "3/ASSAULT/SEXUAL ASSAULT - NOT DANGEROUS BODY AREA",
      "ASLT/SA-ASLT-NR",    "3/ASSAULT/SEXUAL ASSAULT - NON-RECENT (OVER 6 HRS) INJURIES (WITHOUT PRIORITY SYMPTOMS)",
      "ASLT/SA-ASLT-PD",    "2/ASSAULT/SEXUAL ASSAULT - POSSIBLY DANGEROUS BODY AREA",
      "ASLT/SA-ASLT-SH",    "2/ASSAULT/SEXUAL ASSAULT - SERIOUS HEMORRHAGE",
      "ASLT/SA-ASLT-UA",    "1/ASSAULT/SEXUAL ASSAULT - UNCONSCIOUS OR ARREST",
      "ASLT/SA-ASLT-US",    "2/ASSAULT/SEXUAL ASSAULT - UNKNOWN STATUS",
      "ASLT/SA-SA-CN",      "1/ASSAULT/SEXUAL ASSAULT - CHEST OR NECK INJURY (WITH DIFF BREATHING)",
      "ASLT/SA-SA-MV",      "1/ASSAULT/SEXUAL ASSAULT - MULTIPLE VICTIMS",
      "ASLT/SA-SA-NA",      "1/ASSAULT/SEXUAL ASSAULT - NOT ALERT",
      "ASLT/SA-SA-ND",      "3/ASSAULT/SEXUAL ASSAULT - NOT DANGEROUS BODY AREA",
      "ASLT/SA-SA-NR",      "3/ASSAULT/SEXUAL ASSAULT - NON-RECENT (OVER 6 HRS) INJURIES (WITHOUT PRIORITY SYMPTOMS)",
      "ASLT/SA-SA-P",       "2/ASSAULT/SEXUAL ASSAULT - IN PROGRESS/JUST OCCURRED",
      "ASLT/SA-SA-PD",      "2/ASSAULT/SEXUAL ASSAULT - POSSIBLY DANGEROUS BODY AREA",
      "ASLT/SA-SA-SH",      "2/ASSAULT/SEXUAL ASSAULT - SERIOUS HEMORRHAGE",
      "ASLT/SA-SA-UA",      "1/ASSAULT/SEXUAL ASSAULT - UNCONSCIOUS OR ARREST",
      "ASLT/SA-SA-US",      "2/ASSAULT/SEXUAL ASSAULT - UNKNOWN STATUS",
      "BACK" ,              "1/BACK PAIN (NON-TRAUMATIC OR NON-RECENT TRAUMA)",
      "BACK-FAINT",         "2/BACK PAIN (NON-TRAUMATIC OR NON-RECENT TRAUMA) - FAINTING OR NEAR FAINTING OVER 50",
      "BACK-KAA",           "2/BACK PAIN (NON-TRAUMATIC OR NON-RECENT TRAUMA) - KNOWN AORTIC ANEURYSM",
      "BACK-NA",            "1/BACK PAIN (NON-TRAUMATIC OR NON-RECENT TRAUMA) - NOT ALERT",
      "BACK-NONT",          "3/BACK PAIN (NON-TRAUMATIC OR NON-RECENT TRAUMA) - NON TRAUMATIC BACK PAIN",
      "BACK-NR",            "3/BACK PAIN (NON-TRAUMATIC OR NON-RECENT TRAUMA) - NON-RECENT (OVER 6 HRS) TRAUMATIC BACK PAIN",
      "BACK-SAA",           "2/BACK PAIN (NON-TRAUMATIC OR NON-RECENT TRAUMA) - SUSPECTED AORTIC ANEURYSM",
      "BOMB-DEVICE",        "2/BOMB INCIDENT - DEVICE",
      "BP",                 "2/BREATHING PROBLEMS",
      "BP-AB",              "2/BREATHING PROBLEMS - ABNORMAL BREATHING",
      "BP-CC",              "1/BREATHING PROBLEMS - CHANGING COLOR",
      "BP-CLAMMY",          "1/BREATHING PROBLEMS - CLAMMY",
      "BP-DS",              "1/BREATHING PROBLEMS - DIFFICULTY SPEAKING BETWEEN BREATHS",
      "BP-IB",              "1/BREATHING PROBLEMS - INEFFECTIVE BREATHING",
      "BP-NA",              "1/BREATHING PROBLEMS - NOT ALERT",
      "BURN",               "2/BURNS (SCALDS)/EXPLOSION (BLAST)",
      "BURN-BF",            "1/BURNS (SCALDS)/EXPLOSION (BLAST) - BUILDING FIRE WITH PERSONS REPORTED INSIDE",
      "BURN-BLAST",         "2/BURNS (SCALDS)/EXPLOSION (BLAST) - BLAST INJURIES (W/OUT PRIORITY SYMPTOMS)",
      "BURN-BURN<18%",      "3/BURNS (SCALDS)/EXPLOSION (BLAST) - BURNS UNDER  18% BODY AREA",
      "BURN-BURN>18%",      "1/BURNS (SCALDS)/EXPLOSION (BLAST) - BURNS OVER 18% BODY AREA",
      "BURN-DB",            "1/BURNS (SCALDS)/EXPLOSION (BLAST) - DIFFICULTY BREATHING",
      "BURN-DS",            "1/BURNS (SCALDS)/EXPLOSION (BLAST) - DIFFICULTY SPEAKING BETWEEN BREATHS",
      "BURN-FA",            "3/BURNS (SCALDS)/EXPLOSION (BLAST) - FIRE ALARM (UNK SITUATION)",
      "BURN-FACE",          "1/BURNS (SCALDS)/EXPLOSION (BLAST) - SIGNIFICANT FACIAL BURNS",
      "BURN-MV",            "1/BURNS (SCALDS)/EXPLOSION (BLAST) - MULTIPLE VICTIMS",
      "BURN-NA",            "1/BURNS (SCALDS)/EXPLOSION (BLAST) - NOT ALERT",
      "BURN-PERSON",        "1/BURNS (SCALDS)/EXPLOSION (BLAST) - PERSON ON FIRE",
      "BURN-SUN",           "3/BURNS (SCALDS)/EXPLOSION (BLAST) - SUNBURN OR MINOR BURNS (< HAND SIZE)",
      "BURN-UA",            "1/BURNS (SCALDS)/EXPLOSION (BLAST) - UNCONSCIOUS OR ARREST",
      "BURN-US",            "2/BURNS (SCALDS)/EXPLOSION (BLAST) - UNKNOWN STATUS",
      "CARDIAC",            "2/CARDIAC OR RESPIRATORY ARREST/DEATH",
      "CARDIAC-BU",         "1/CARDIAC OR RESPIRATORY ARREST/DEATH - BREATHING UNCERTAIN (AGONAL)",
      "CARDIAC-ED",         "3/CARDIAC OR RESPIRATORY ARREST/DEATH - EXPECTED DEATH UNQUESTIONABLE",
      "CARDIAC-HANG",       "1/CARDIAC OR RESPIRATORY ARREST/DEATH - HANGING",
      "CARDIAC-IB",         "1/CARDIAC OR RESPIRATORY ARREST/DEATH - INEFFECTIVE BREATHING",
      "CARDIAC-NB",         "1/CARDIAC OR RESPIRATORY ARREST/DEATH - NOT BREATHING AT ALL",
      "CARDIAC-OD",         "3/CARDIAC OR RESPIRATORY ARREST/DEATH - OBVIOUS DEATH UNQUESTIONABLE",
      "CARDIAC-OED",        "1/CARDIAC OR RESPIRATORY ARREST/DEATH - OBVIOUS OR EXPECTED DEATH QUESTIONABLE",
      "CARDIAC-STRANG",     "1/CARDIAC OR RESPIRATORY ARREST/DEATH - STRANGULATION",
      "CARDIAC-SUFF",       "1/CARDIAC OR RESPIRATORY ARREST/DEATH - SUFFOCATION",
      "CARDIAC-UNDER",      "1/CARDIAC OR RESPIRATORY ARREST/DEATH - UNDERWATER",
      "CHEST",              "2/CHEST PAIN (NON-TRAUMATIC)",
      "CHEST-AB",           "2/CHEST PAIN (NON-TRAUMATIC) - ABNORMAL BREATHING",
      "CHEST-BN<35",        "3/CHEST PAIN (NON-TRAUMATIC) - BREATHING NORMALLY UNDER 35",
      "CHEST-BN>35",        "2/CHEST PAIN (NON-TRAUMATIC) - BREATHING NORMALLY OVER 35",
      "CHEST-CC",           "1/CHEST PAIN (NON-TRAUMATIC) - CHANGING COLOR",
      "CHEST-CLAMMY",       "1/CHEST PAIN (NON-TRAUMATIC) - CLAMMY",
      "CHEST-COC",          "2/CHEST PAIN (NON-TRAUMATIC) - COCAINE",
      "CHEST-DS",           "1/CHEST PAIN (NON-TRAUMATIC) - DIFFICULTY SPEAKING BETWEEN BREATHS",
      "CHEST-HIST",         "2/CHEST PAIN (NON-TRAUMATIC) - HEART ATTACK OR ANGINA HISTORY",
      "CHEST-NA",           "1/CHEST PAIN (NON-TRAUMATIC) - NOT ALERT",
      "CHILD-VEH",          "2/ENDANGERED - LOCKED IN VEHICLE",
      "CHOKE",              "2/CHOKING",
      "CHOKE-AB",           "1/CHOKING - ABNORMAL BREATHING (PARTIAL OBSTRUCTION)",
      "CHOKE-IB",           "1/CHOKING - COMPLETE OBSTRUCTION/INEFFECTIVE BREATHING",
      "CHOKE-NA",           "1/CHOKING - NOT ALERT",
      "CHOKE-NC",           "3/CHOKING - NOT CHOKING NOW (ALERT AND BREATHING NORMALLY)",
      "CM/HAZMAT",          "1/CARBON MONOXIDE/INHALATION/HAZMAT/CBRN",
      "CM/HAZMAT-ALERT/DB", "1/CARBON MONOXIDE/INHALATION/HAZMAT/CBRN - ALERT WITH DIFF BREATHING",
      "CM/HAZMAT-CM",       "3/CARBON MONOXIDE/INHALATION/HAZMAT/CBRN - CARBON MONOXIDE DETECTOR ALARM",
      "CM/HAZMAT-DS",       "1/CARBON MONOXIDE/INHALATION/HAZMAT/CBRN - DIFFICULTY SPEAKING BETWEEN BREATHS",
      "CM/HAZMAT-MV",       "1/CARBON MONOXIDE/INHALATION/HAZMAT/CBRN - MULTIPLE VICTIMS",
      "CM/HAZMAT-NA",       "1/CARBON MONOXIDE/INHALATION/HAZMAT/CBRN - NOT ALERT",
      "CM/HAZMAT-NDB",      "2/CARBON MONOXIDE/INHALATION/HAZMAT/CBRN - ALERT W/O DIFF BREATHING",
      "CM/HAZMAT-UA",       "1/CARBON MONOXIDE/INHALATION/HAZMAT/CBRN - UNCONSCIOUS OR ARREST",
      "CM/HAZMAT-US",       "1/CARBON MONOXIDE/INHALATION/HAZMAT/CBRN - UNKNOWN STATUS",
      "CONTROL-ILLEGAL",    "2/CONTROL BURN - ILLEGAL OR OUT OF CONTROL",
      "CONTROL-UC",         "3/CONTROL BURN - UNCONFIRMED",
      "CS",                 "1/CONVULSIONS/SEIZURES",
      "CS-AG/IB",           "1/CONVULSIONS/SEIZURES - AGONAL/INEFFECTIVE BREATHING",
      "CS-AS",              "3/CONVULSIONS/SEIZURES - ATYPICAL SEIZURE",
      "CS-DIAB",            "2/CONVULSIONS/SEIZURES - DIABETIC",
      "CS-EB<35",           "2/CONVULSIONS/SEIZURES - EFFECTIVE BREATHING NOT VERIFIED UNDER 35",
      "CS-EB>35",           "1/CONVULSIONS/SEIZURES - EFFECTIVE BREATHING NOT VERIFIED OVER 35",
      "CS-FA",              "3/CONVULSIONS/SEIZURES - FOCAL SEIZURE (ALERT)",
      "CS-FNA",             "2/CONVULSIONS/SEIZURES - FOCAL SEIZURE (NOT ALERT)",
      "CS-HIST",            "2/CONVULSIONS/SEIZURES - HISTORY OF STROKE OR BRAIN TUMOR",
      "CS-IMP",             "3/CONVULSIONS/SEIZURES - IMPENDING SEIZURE (AURA)",
      "CS-MULT",            "1/CONVULSIONS/SEIZURES - CONTINOUS OR MULTIPLE SEIZURES",
      "CS-NB",              "1/CONVULSIONS/SEIZURES - NOT BREATHING (AFTER KEY QUESTIONS)",
      "CS-NS",              "3/CONVULSIONS/SEIZURES - NOT SEIZING AND BREATHING EFFECTIVELY",
      "CS-NS<6",            "3/CONVULSIONS/SEIZURES - NOT SEIZING AND EFFECTIVE BREATHING UNDER 6",
      "CS-NS>6",            "3/CONVULSIONS/SEIZURES - NOT SEIZING AND EFFECTIVE BREATHING OVER 6",
      "CS-OD",              "2/CONVULSIONS/SEIZURES - OVERDOSE/POISIONING",
      "CS-PREG",            "2/CONVULSIONS/SEIZURES - PREGNANCY",
      "DDSA",               "2/DROWNING(NEAR)/DIVING/SCUBA ACCIDENT",
      "DDSA-AB",            "2/DROWNING(NEAR)/DIVING/SCUBA ACCIDENT - ALERT WITH ABNORMAL BREATHING",
      "DDSA-ALERT",         "3/DROWNING(NEAR)/DIVING/SCUBA ACCIDENT - ALERT AND BREATHING NORMALLY/NO INJ",
      "DDSA-DIVING",        "1/DROWNING(NEAR)/DIVING/SCUBA ACCIDENT - DIVING OR SUSPECTED NECK INJ",
      "DDSA-INJ",           "2/DROWNING(NEAR)/DIVING/SCUBA ACCIDENT - ALERT AND BREATHING NORMALLY/INJ OR IN WATER",
      "DDSA-NA",            "1/DROWNING(NEAR)/DIVING/SCUBA ACCIDENT - NOT ALERT",
      "DDSA-SCUBA",         "1/DROWNING(NEAR)/DIVING/SCUBA ACCIDENT - SCUBA ACCIDENT",
      "DDSA-UA",            "1/DROWNING(NEAR)/DIVING/SCUBA ACCIDENT - UNCONSCIOUS/ARREST",
      "DDSA-US",            "2/DROWNING(NEAR)/DIVING/SCUBA ACCIDENT - UNKNOWN STATUS",
      "DIAB",               "1/DIABETIC PROBLEMS",
      "DIAB-AB",            "2/DIABETIC PROBLEMS - ABNORMAL BREATHING",
      "DIAB-ABN",           "2/DIABETIC PROBLEMS - ABNORMAL BEHAVIOR",
      "DIAB-ALERT",         "3/DIABETIC PROBLEMS - ALERT AND BREATHING NORMALLY",
      "DIAB-NA",            "2/DIABETIC PROBLEMS - NOT ALERT",
      "DIAB-UNC",           "1/DIABETIC PROBLEMS - UNCONSCIOUS",
      "EL",                 "2/ELECTROCUTION/LIGHTNING",
      "EL-AB",              "1/ELECTROCUTION/LIGHTNING - ABNORMAL BREATHING",
      "EL-ALERT",           "2/ELECTROCUTION/LIGHTNING - ALERT AND BREATHING NORMALLY",
      "EL-EXTREME",         "1/ELECTROCUTION/LIGHTNING - EXTREME FALL",
      "EL-LONG",            "1/ELECTROCUTION/LIGHTNING - LONG FALL",
      "EL-NA",              "1/ELECTROCUTION/LIGHTNING - NOT ALERT",
      "EL-NB/IB",           "1/ELECTROCUTION/LIGHTNING - NOT BREATHING/INEFFECTIVE BREATHING",
      "EL-ND",              "1/ELECTROCUTION/LIGHTNING - NOT DISCONNECTED FROM POWER",
      "EL-POWER",           "1/ELECTROCUTION/LIGHTNING - POWER NOT OFF/HAZARD PRESENT",
      "EL-UNC",             "1/ELECTROCUTION/LIGHTNING - UNCONSCIOUS",
      "EL-US",              "1/ELECTROCUTION/LIGHTNING - UNKNOWN STATUS",
      "EST-GRASS",          "1/EAST SIDE TASK FORCE - GRASS FIRE",
      "EXPLOSION",          "1/EXPLOSION",
      "EXPOSURE",           "1/HEAT/COLD EXPOSURE",
      "EXPOSURE-ALERT",     "3/HEAT/COLD EXPOSURE - ALERT",
      "EXPOSURE-CC",        "2/HEAT/COLD EXPOSURE - CHANGE IN SKIN COLOR",
      "EXPOSURE-HIST",      "2/HEAT/COLD EXPOSURE - HEART ATTACK OR ANGINA HISTORY",
      "EXPOSURE-MV",        "1/HEAT/COLD EXPOSURE - MULTIPLE VICTIMS",
      "EXPOSURE-NA",        "1/HEAT/COLD EXPOSURE - NOT ALERT",
      "EXPOSURE-US",        "2/HEAT/COLD EXPOSURE - UNKNOWN STATUS",
      "EYE",                "3/EYE PROBLEMS/INJURIES",
      "EYE-MED",            "3/EYE PROBLEMS/INJURIES - MEDICAL EYE PROBLEMS",
      "EYE-MINOR",          "3/EYE PROBLEMS/INJURIES - MINOR EYE INJURIES",
      "EYE-MOD",            "3/EYE PROBLEMS/INJURIES - MODERATE EYE INJURIES",
      "EYE-NA",             "1/EYE PROBLEMS/INJURIES - NOT ALERT",
      "EYE-SEVERE",         "2/EYE PROBLEMS/INJURIES - SEVERE EYE INJURIES",
      "FALLS",              "1/FALLS",
      "FALLS-CN",           "1/FALLS - CHEST OR NECK INJURY",
      "FALLS-EXTREME",      "1/FALLS - EXTREME FALL",
      "FALLS-LONG",         "1/FALLS - LONG FALL",
      "FALLS-NA",           "1/FALLS - NOT ALERT",
      "FALLS-ND",           "3/FALLS - NOT DANGEROUS BODY AREA",
      "FALLS-NR",           "3/FALLS - NON-RECENT",
      "FALLS-PA",           "3/FALLS - PUBLIC ASSIST",
      "FALLS-PD",           "2/FALLS - POSSIBLY DANGEROUS BODY AREA",
      "FALLS-SH",           "2/FALLS - SERIOUS HEMORRHAGE",
      "FALLS-UA",           "1/FALLS - UNCONSCIOUS OR ARREST",
      "FALLS-US",           "2/FALLS - UNKNOWN STATUS",
      "FDA",                "3/FD ASSISTANCE (LADDER, BEES, EQUIP)",
      "FIGHT-WPN/INJ",      "2/FIGHT - WEAPONS OR INJURIES",
      "GAS",                "1/ODOR OF OR GAS LEAK",
      "GAS-LB",             "1/ODOR OF OR GAS LEAK - LINEBREAK",
      "GAS-SMELL",          "1/ODOR OF OR GAS LEAK - SMELL",
      "GRASS",              "1/GRASS FIRE",
      "GRASS-NOSTRUCT",     "2/GRASS FIRE - NO STRUCTURE THREATENED",
      "GRASS-SMOKE",        "2/GRASS FIRE - SMOKE ONLY",
      "GRASS-STRUCT",       "1/GRASS FIRE - STRUCTURE THREATENED",
      "HAZMAT",             "1/HAZARDOUS MATERIAL INCIDENT",
      "HEADACHE",           "2/HEADACHE",
      "HEADACHE-AB",        "2/HEADACHE - ABNORMAL BREATHING",
      "HEADACHE-BN",        "3/HEADACHE - BREATHING NORMALLY",
      "HEADACHE-CB",        "2/HEADACHE - CHANGE IN BEHAVIOR",
      "HEADACHE-NA",        "2/HEADACHE - NOT ALERT",
      "HEADACHE-NUMB",      "2/HEADACHE - NUMBNESS",
      "HEADACHE-PARA",      "2/HEADACHE - PARALYSIS",
      "HEADACHE-SOP",       "2/HEADACHE - SUDDEN ONSET OF SEVERE PAIN",
      "HEADACHE-SPEECH",    "2/HEADACHE - SPEECH PROBLEMS",
      "HEADACHE-US",        "2/HEADACHE - UNKNOWN STATUS",
      "HEART",              "2/HEART PROBLEMS /A.I.C.D.",
      "HEART-AB",           "2/HEART PROBLEMS /A.I.C.D. - ABNORMAL BREATHING",
      "HEART-CC",           "1/HEART PROBLEMS /A.I.C.D. - CHANGING COLOR",
      "HEART-CLAMMY",       "1/HEART PROBLEMS /A.I.C.D. - CLAMMY",
      "HEART-COC",          "2/HEART PROBLEMS /A.I.C.D. - COCAINE",
      "HEART-CP<35",        "3/HEART PROBLEMS /A.I.C.D. - CHEST PAIN UNDER 35",
      "HEART-CP>35",        "2/HEART PROBLEMS /A.I.C.D. - CHEST PAIN OVER 35",
      "HEART-DS",           "1/HEART PROBLEMS /A.I.C.D. - DIFFICULTY SPEAKING BETWEEN BREATHS",
      "HEART-FA",           "2/HEART PROBLEMS /A.I.C.D. - FIRING OF A.I.C.D",
      "HEART-HIST",         "2/HEART PROBLEMS /A.I.C.D. - CARDIAC HISTORY",
      "HEART-HR2",          "2/HEART PROBLEMS /A.I.C.D. - HEART RATE UNDER 50 OR OVER 130",
      "HEART-HR3",          "3/HEART PROBLEMS /A.I.C.D. - HEART RATE OVER 50 AND UNDER 130",
      "HEART-JR",           "1/HEART PROBLEMS /A.I.C.D. - JUST RESUSITATED/DEFIBRILLATED",
      "HEART-NA",           "1/HEART PROBLEMS /A.I.C.D. - NOT ALERT",
      "HEART-US",           "2/HEART PROBLEMS /A.I.C.D. - UNKNOWN STATUS",
      "HEM/LAC",            "1/HEMORRHAGE/LACERATIONS",
      "HEM/LAC-AB",         "1/HEMORRHAGE/LACERATIONS - ABNORMAL BREATHING",
      "HEM/LAC-BD",         "2/HEMORRHAGE/LACERATIONS - BLEEDING DISORDER",
      "HEM/LAC-BT",         "2/HEMORRHAGE/LACERATIONS - BLOOD THINNERS",
      "HEM/LAC-DH",         "1/HEMORRHAGE/LACERATIONS - DANGEROUS HEMORRHAGE",
      "HEM/LAC-HD",         "2/HEMORRHAGE/LACERATIONS - HEMORRHAGE OF DIALYSIS FISTULA",
      "HEM/LAC-HT",         "2/HEMORRHAGE/LACERATIONS - HEMORRHAGE THROUGH TUBES",
      "HEM/LAC-MINOR",      "3/HEMORRHAGE/LACERATIONS - MINOR HEMORRHAGE",
      "HEM/LAC-NA",         "1/HEMORRHAGE/LACERATIONS - NOT ALERT",
      "HEM/LAC-ND",         "3/HEMORRHAGE/LACERATIONS - NOT DANGEROUS HEMORRHAGE",
      "HEM/LAC-PD",         "2/HEMORRHAGE/LACERATIONS - POSSIBLY DANGEROUS HEMORRHAGE",
      "HEM/LAC-SH",         "2/HEMORRHAGE/LACERATIONS - SERIOUS HEMORRHAGE",
      "HEM/LAC-UA",         "1/HEMORRHAGE/LACERATIONS - UNCONSCIOUS OR ARREST",
      "HIWATER-P",          "1/HIGH WATER - PERSON IN WATER",
      "HIWATER-U",          "2/HIGH WATER - UNCONFIRMED PERSON IN WATER",
      "IA",                 "2/INACCESSIBLE INCIDENT/OTHER ENTRAPMENT",
      "IA-CSE",             "1/INACCESSIBLE INCIDENT/OTHER ENTRAPMENT - CONFINED SPACE ENTRAPMENT",
      "IA-ITS",             "1/INACCESSIBLE INCIDENT/OTHER ENTRAPMENT - INACCESIBLE TERRAIN SITUATION",
      "IA-MA",              "1/INACCESSIBLE INCIDENT/OTHER ENTRAPMENT - MUDSLIDE/AVALANCHE",
      "IA-MM",              "1/INACCESSIBLE INCIDENT/OTHER ENTRAPMENT - MECHANICAL/MACHINERY ENTRAPMENT",
      "IA-NT",              "3/INACCESSIBLE INCIDENT/OTHER ENTRAPMENT - NO LONGER TRAPPED NO INJ",
      "IA-NTU",             "2/INACCESSIBLE INCIDENT/OTHER ENTRAPMENT - NO LONGER TRAPPED UNK INJ",
      "IA-PE",              "2/INACCESSIBLE INCIDENT/OTHER ENTRAPMENT - PERIPHERAL ENTRAPMENT",
      "IA-SC",              "1/INACCESSIBLE INCIDENT/OTHER ENTRAPMENT - STRUCTURE COLLAPSE",
      "IA-TC",              "1/INACCESSIBLE INCIDENT/OTHER ENTRAPMENT - TRENCH COLLAPSE",
      "IA-US",              "2/INACCESSIBLE INCIDENT/OTHER ENTRAPMENT - UNKNOWN STATUS",
      "MUTAID",             "1/MUTUAL AID",
      "OD",                 "1/OVERDOSE/POISONING",
      "OD-A",               "2/OVERDOSE/POISONING - ANTIDEPRESSANTS",
      "OD-AA",              "2/OVERDOSE/POISONING - ACID OR ALKALI",
      "OD-AB",              "2/OVERDOSE/POISONING - ABNORMAL BREATHING",
      "OD-CC",              "1/OVERDOSE/POISONING - CHANGING COLOR",
      "OD-CM",              "2/OVERDOSE/POISONING - COCAINE, METHAMPHETAMINE",
      "OD-NA",              "2/OVERDOSE/POISONING - NOT ALERT",
      "OD-NARC",            "2/OVERDOSE/POISONING - NARCOTICS",
      "OD-OD",              "3/OVERDOSE/POISONING - OVERDOSE NO PRIORITY SYMPTOMS",
      "OD-PC",              "2/OVERDOSE/POISONING - POISON CONTROL",
      "OD-POISON",          "3/OVERDOSE/POISONING - POISONING NO PRIORITY SYMPTOMS",
      "OD-UNC",             "1/OVERDOSE/POISONING - UNCONSCIOUS",
      "OD-US",              "2/OVERDOSE/POISONING - UNKNOWN STATUS/OTHER CODES NA",
      "PREG",               "2/PREGNANCY/CHILDBIRTH/MISCARRIAGE",
      "PREG-1TRI",          "3/PREGNANCY/CHILDBIRTH/MISCARRIAGE - 1ST TRIMESTER HERMORRHAGE OR MISCARRIAGE",
      "PREG-1TRI-SH",       "2/PREGNANCY/CHILDBIRTH/MISCARRIAGE - 1ST TRIMESTER SERIOUS HEMORRHAGE",
      "PREG-2TRI",          "2/PREGNANCY/CHILDBIRTH/MISCARRIAGE - 2ND TRIMESTER HEMORRHAGE OR MISCARRIAGE",
      "PREG-3TRI",          "1/PREGNANCY/CHILDBIRTH/MISCARRIAGE - 3RD TRIMESTER HEMORRHAGE",
      "PREG-BBCB",          "1/PREGNANCY/CHILDBIRTH/MISCARRIAGE - BABY BORN COMPLICATIONS  W/BABY",
      "PREG-BBCM",          "1/PREGNANCY/CHILDBIRTH/MISCARRIAGE - BABY BORN COMPLICATIONS W/MOTHER",
      "PREG-BC",            "1/PREGNANCY/CHILDBIRTH/MISCARRIAGE - BREECH OR CORD",
      "PREG-BORN",          "2/PREGNANCY/CHILDBIRTH/MISCARRIAGE - BABY BORN (NO COMPLICATIONS)",
      "PREG-HRC",           "1/PREGNANCY/CHILDBIRTH/MISCARRIAGE - HIGH RISK COMPLICATIONS",
      "PREG-HV",            "1/PREGNANCY/CHILDBIRTH/MISCARRIAGE - HEAD VISIBLE/OUT",
      "PREG-ID",            "1/PREGNANCY/CHILDBIRTH/MISCARRIAGE - IMMINENT DELIVERY",
      "PREG-LABOR",         "2/PREGNANCY/CHILDBIRTH/MISCARRIAGE - LABOR (DELIVERY NOT IMMINENT)",
      "PREG-US",            "2/PREGNANCY/CHILDBIRTH/MISCARRIAGE - UNKNOWN STATUS/OTHER CODES NA",
      "PREG-WB",            "3/PREGNANCY/CHILDBIRTH/MISCARRIAGE - WATERS BROKEN (NO CONTRACTIONS)",
      "PSYCH",              "2/PSYCHIATRIC/ABNORMAL BEHAVIOR/SUICIDE ATTEMPT",
      "PSYCH-DH",           "1/PSYCHIATRIC/ABNORMAL BEHAVIOR/SUICIDE ATTEMPT - DANGEROUS HEMORRHAGE",
      "PSYCH-JUMP",         "2/PSYCHIATRIC/ABNORMAL BEHAVIOR/SUICIDE ATTEMPT - JUMPER (THREATENING)",
      "PSYCH-NA",           "1/PSYCHIATRIC/ABNORMAL BEHAVIOR/SUICIDE ATTEMPT - NOT ALERT",
      "PSYCH-NH",           "2/PSYCHIATRIC/ABNORMAL BEHAVIOR/SUICIDE ATTEMPT - NEAR HANGING,STRANGULATION OR SUFFOCATION",
      "PSYCH-NSA",          "3/PSYCHIATRIC/ABNORMAL BEHAVIOR/SUICIDE ATTEMPT - NON-SUICIDIAL AND ALERT",
      "PSYCH-NSH",          "2/PSYCHIATRIC/ABNORMAL BEHAVIOR/SUICIDE ATTEMPT - NON SERIOUS/MINOR HEMORRHAGE",
      "PSYCH-SA",           "3/PSYCHIATRIC/ABNORMAL BEHAVIOR/SUICIDE ATTEMPT - SUICIDAL (NOT THREATENING) AND ALERT",
      "PSYCH-SH",           "2/PSYCHIATRIC/ABNORMAL BEHAVIOR/SUICIDE ATTEMPT - SERIOUS HEMORRHAGE",
      "PSYCH-THREAT",       "2/PSYCHIATRIC/ABNORMAL BEHAVIOR/SUICIDE ATTEMPT - THREATENING SUICIDE",
      "PSYCH-US",           "2/PSYCHIATRIC/ABNORMAL BEHAVIOR/SUICIDE ATTEMPT - UNKNOWN STATUS",
      "SICK",               "2/SICK PERSON",
      "SICK-AB",            "2/SICK PERSON - ABNORMAL BREATHING",
      "SICK-ABNBP",         "3/SICK PERSON - BLOOD PRESSURE ABNORMALITY",
      "SICK-ALC",           "2/SICK PERSON - ALTERED LEVEL OF CONSCIOUSNESS",
      "SICK-BOIL",          "3/SICK PERSON - BOILS",
      "SICK-BUMP",          "3/SICK PERSON - BUMPS (NON-TRAUMATIC)",
      "SICK-CATH",          "3/SICK PERSON - CATHETER (IN/OUT WITHOUT HEMORRHAGING)",
      "SICK-CONST",         "3/SICK PERSON - CONSTIPATION",
      "SICK-COR",           "3/SICK PERSON - CUT-OFF RING REQUEST",
      "SICK-CRAMP",         "3/SICK PERSON - CRAMPS/SPASMS (IN EXTREMITIES AND NON-TRAUMATIC)",
      "SICK-DEAF",          "3/SICK PERSON - DEAFNESS",
      "SICK-DIAR",          "3/SICK PERSON - DEFECATION/DIARRHEA",
      "SICK-DIZZY",         "3/SICK PERSON - DIZZINESS/VERTIGO",
      "SICK-EAR",           "3/SICK PERSON - EARACHE",
      "SICK-ENEMA",         "3/SICK PERSON - ENEMA",
      "SICK-F/C",           "3/SICK PERSON - FEVER/CHILLS",
      "SICK-GOUT",          "3/SICK PERSON - GOUT",
      "SICK-HEM",           "3/SICK PERSON - HEMORRHOIDS/PILES",
      "SICK-HEP",           "3/SICK PERSON - HEPATITIS",
      "SICK-HIC",           "3/SICK PERSON - HICCUPS",
      "SICK-ILL",           "3/SICK PERSON - UNWELL/ILL",
      "SICK-IMM",           "3/SICK PERSON - NEW ONSET OF IMMOBILITY",
      "SICK-ITCH",          "3/SICK PERSON - ITCHING",
      "SICK-NA",            "1/SICK PERSON - NOT ALERT",
      "SICK-NAUSEA",        "3/SICK PERSON - NAUSEA",
      "SICK-NERV",          "3/SICK PERSON - NERVOUS",
      "SICK-NOURI",         "3/SICK PERSON - CAN'T URINATE (W/OUT ABDOMINAL PAIN)",
      "SICK-NPC",           "3/SICK PERSON - NON-PRIORITY COMPLAINTS",
      "SICK-NPS",           "3/SICK PERSON - NO PRIORITY SYMPTOMS",
      "SICK-OBST",          "3/SICK PERSON - OBJECT STUCK",
      "SICK-OBSW",          "3/SICK PERSON - OBJECT SWALLOWED",
      "SICK-OP",            "3/SICK PERSON - OTHER PAIN",
      "SICK-PAINURI",       "3/SICK PERSON - PAINFUL URINATION",
      "SICK-PP",            "3/SICK PERSON - PENIS PROBLEMS/PAIN",
      "SICK-RASH",          "3/SICK PERSON - RASH/SKIN DISORDER (W/OUT DIFFICULTY BREATHING OR SWALLOWING)",
      "SICK-SC",            "2/SICK PERSON - SICKLE CELL CRISIS/THALASSEMIA",
      "SICK-SLEEP",         "3/SICK PERSON - CAN'T SLEEP",
      "SICK-STD",           "3/SICK PERSON - SEXUALLY TRANSMITTED DISEASE",
      "SICK-THROAT",        "3/SICK PERSON - SORE THROAT",
      "SICK-TOOTH",         "3/SICK PERSON - TOOTHACHE (W/OUT JAW PAIN)",
      "SICK-TRANSP",        "3/SICK PERSON - TRANSPORTATION ONLY",
      "SICK-US",            "2/SICK PERSON - UNKNOWN STATUS",
      "SICK-VOMIT",         "3/SICK PERSON - VOMITING",
      "SICK-WEAK",          "3/SICK PERSON - GENERAL WEAKNESS",
      "SICK-WI",            "3/SICK PERSON - WOUND INFECTED (FOCAL OR SURFACE)",
      "SPILL",              "1/SPILL CONTROL",
      "STAB/GUN",           "2/STAB/GUNSHOT/PENETRATING TRAUMA",
      "STAB/GUN-GUN-CENT",  "1/STAB/GUNSHOT/PENETRATING TRAUMA - CENTRAL WOUNDS",
      "STAB/GUN-GUN-KSP",   "2/STAB/GUNSHOT/PENETRATING TRAUMA - KNOWN SINGLE PERIPHERAL WOUND",
      "STAB/GUN-GUN-MV",    "1/STAB/GUNSHOT/PENETRATING TRAUMA - MULTIPLE VICTIMS",
      "STAB/GUN-GUN-MW",    "1/STAB/GUNSHOT/PENETRATING TRAUMA - MULTIPLE WOUNDS",
      "STAB/GUN-GUN-NA",    "1/STAB/GUNSHOT/PENETRATING TRAUMA - NOT ALERT",
      "STAB/GUN-GUN-NRC",   "2/STAB/GUNSHOT/PENETRATING TRAUMA - NON-RECENT SINGLE CENTRAL WOUND",
      "STAB/GUN-GUN-NRP",   "3/STAB/GUNSHOT/PENETRATING TRAUMA - NON-RECENT PERIPHERAL WOUNDS",
      "STAB/GUN-GUN-OD",    "2/STAB/GUNSHOT/PENETRATING TRAUMA - OBVIOUS DEATH",
      "STAB/GUN-GUN-SH",    "2/STAB/GUNSHOT/PENETRATING TRAUMA - SERIOUS HEMORRHAGE",
      "STAB/GUN-GUN-UA",    "1/STAB/GUNSHOT/PENETRATING TRAUMA - UNCONSCIOUS OR ARREST",
      "STAB/GUN-GUN-US",    "2/STAB/GUNSHOT/PENETRATING TRAUMA - UNKNOWN STATUS",
      "STAB/GUN-PT-CENT",   "1/STAB/GUNSHOT/PENETRATING TRAUMA - CENTRAL WOUNDS",
      "STAB/GUN-PT-KSP",    "2/STAB/GUNSHOT/PENETRATING TRAUMA - KNOWN SINGLE PERIPHERAL WOUND",
      "STAB/GUN-PT-MV",     "1/STAB/GUNSHOT/PENETRATING TRAUMA - MULTIPLE VICTIMS",
      "STAB/GUN-PT-MW",     "1/STAB/GUNSHOT/PENETRATING TRAUMA - MULTIPLE WOUNDS",
      "STAB/GUN-PT-NA",     "1/STAB/GUNSHOT/PENETRATING TRAUMA - NOT ALERT",
      "STAB/GUN-PT-NRC",    "2/STAB/GUNSHOT/PENETRATING TRAUMA - NON-RECENT SINGLE CENTRAL WOUND",
      "STAB/GUN-PT-NRP",    "3/STAB/GUNSHOT/PENETRATING TRAUMA - NON-RECENT PERIPHERAL WOUNDS",
      "STAB/GUN-PT-OD",     "2/STAB/GUNSHOT/PENETRATING TRAUMA - OBVIOUS DEATH",
      "STAB/GUN-PT-SH",     "2/STAB/GUNSHOT/PENETRATING TRAUMA - SERIOUS HEMORRHAGE",
      "STAB/GUN-PT-UA",     "1/STAB/GUNSHOT/PENETRATING TRAUMA - UNCONSCIOUS OR ARREST",
      "STAB/GUN-PT-US",     "2/STAB/GUNSHOT/PENETRATING TRAUMA - UNKNOWN STATUS",
      "STAB/GUN-STAB-CENT", "1/STAB/GUNSHOT/PENETRATING TRAUMA - CENTRAL WOUNDS",
      "STAB/GUN-STAB-KSP",  "2/STAB/GUNSHOT/PENETRATING TRAUMA - KNOWN SINGLE PERIPHERAL WOUND",
      "STAB/GUN-STAB-MV",   "1/STAB/GUNSHOT/PENETRATING TRAUMA - MULTIPLE VICTIMS",
      "STAB/GUN-STAB-MW",   "1/STAB/GUNSHOT/PENETRATING TRAUMA - MULTIPLE WOUNDS",
      "STAB/GUN-STAB-NA",   "1/STAB/GUNSHOT/PENETRATING TRAUMA - NOT ALERT",
      "STAB/GUN-STAB-NRC",  "2/STAB/GUNSHOT/PENETRATING TRAUMA - NON-RECENT SINGLE CENTRAL WOUND",
      "STAB/GUN-STAB-NRP",  "3/STAB/GUNSHOT/PENETRATING TRAUMA - NON-RECENT PERIPHERAL WOUNDS",
      "STAB/GUN-STAB-OD",   "2/STAB/GUNSHOT/PENETRATING TRAUMA - OBVIOUS DEATH",
      "STAB/GUN-STAB-SH",   "2/STAB/GUNSHOT/PENETRATING TRAUMA - SERIOUS HEMORRHAGE",
      "STAB/GUN-STAB-UA",   "1/STAB/GUNSHOT/PENETRATING TRAUMA - UNCONSCIOUS OR ARREST",
      "STAB/GUN-STAB-US",   "2/STAB/GUNSHOT/PENETRATING TRAUMA - UNKNOWN STATUS",
      "STL",                "1/STILL ALARM CAR DUMPSTER ETC",
      "STL-NOSTRUCT",       "2/STILL ALARM CAR DUMPSTER ETC - NO STRUCTURE THREATENED",
      "STL-STRUCT",         "1/STILL ALARM CAR DUMPSTER ETC - STRUCTURE THREATENED",
      "STROKE",             "2/STROKE",
      "STROKE-AB",          "2/STROKE - ABNORMAL BREATHING",
      "STROKE-BN<35",       "2/STROKE - BREATHING NORMALLY UNDER 35",
      "STROKE-BN>35",       "2/STROKE - BREATHING NORMALLY OVER 35",
      "STROKE-HEAD",        "2/STROKE - SUDDEN ONSET OF SEVERE HEADACHE",
      "STROKE-HIST",        "2/STROKE - STROKE HISTORY",
      "STROKE-NA",          "2/STROKE - NOT ALERT",
      "STROKE-NUMB",        "2/STROKE - NUMBNESS,PARALYSIS,OR MOVEMENT PROBLEMS",
      "STROKE-SPEECH",      "2/STROKE - SPEECH PROBLEMS",
      "STROKE-TIA",         "2/STROKE - MINI-STROKE HISTORY",
      "STROKE-US",          "2/STROKE - UNKNOWN STATUS",
      "STROKE-VISION",      "2/STROKE - VISION PROBLEMS",
      "STRUCT",             "1/STRUCTURE FIRE",
      "STRUCT-COMM",        "1/STRUCTURE FIRE - COMMERCIAL",
      "STRUCT-RES",         "1/STRUCTURE FIRE - RESIDENTIAL",
      "STRUCT-RESAPP",      "2/STRUCTURE FIRE - RESIDENTIAL APPLIANCE CONTAINED,OUTLET OR ELECTRICAL BURN ODOR",
      "STRUCT-RESOUT",      "2/STRUCTURE FIRE - RESIDENTIAL FIRE OUT",
      "TI",                 "2/TRAUMATIC INJURIES (SPECIFIC)",
      "TI-CN",              "1/TRAUMATIC INJURIES (SPECIFIC) - CHEST/NECK INJURY (WITH DIFFICULTY BREATHING",
      "TI-NA",              "1/TRAUMATIC INJURIES (SPECIFIC) - NOT ALERT",
      "TI-ND",              "3/TRAUMATIC INJURIES (SPECIFIC) - NOT DANGEROUS BODY AREA",
      "TI-NR",              "3/TRAUMATIC INJURIES (SPECIFIC) - NON-RECENT",
      "TI-PD",              "2/TRAUMATIC INJURIES (SPECIFIC) - POSSIBLY DANGEROUS BODY AREA",
      "TI-SH",              "2/TRAUMATIC INJURIES (SPECIFIC) - SERIOUS HEMORRHAGE",
      "TI-UA",              "1/TRAUMATIC INJURIES (SPECIFIC) - UNCONSCIOUS OR ARREST",
      "TTA",                "2/TRAFFIC/TRANSPORTATION ACCIDENTS",
      "TTA-1ND",            "3/TRAFFIC/TRANSPORTATION ACCIDENTS - 1ST PARTY CALLER INJ TO NOT DANGEROUS BODY AREA",
      "TTA-HAZMAT",         "1/TRAFFIC/TRANSPORTATION ACCIDENTS - HAZMAT",
      "TTA-HM-ATS",         "1/TRAFFIC/TRANSPORTATION ACCIDENTS - HIGH MECHANISM - ALL TERRAIN/SNOWMOBILE",
      "TTA-HM-BIKE",        "1/TRAFFIC/TRANSPORTATION ACCIDENTS - HIGH MECHANISM - AUTO-BICYCLE/MOTORCYCLE",
      "TTA-HM-EJECT",       "1/TRAFFIC/TRANSPORTATION ACCIDENTS - HIGH MECHANISM - EJECTION",
      "TTA-HM-PD",          "1/TRAFFIC/TRANSPORTATION ACCIDENTS - HIGH MECHANISM - POSSIBLE DEATH",
      "TTA-HM-PED",         "1/TRAFFIC/TRANSPORTATION ACCIDENTS - HIGH MECHANISM - AUTO-PEDESTRIAN",
      "TTA-HM-ROLL",        "1/TRAFFIC/TRANSPORTATION ACCIDENTS - HIGH MECHANISM - ROLLOVERS",
      "TTA-HM-SINK",        "1/TRAFFIC/TRANSPORTATION ACCIDENTS - HIGH MECHANISM - SINKING VEHICLE",
      "TTA-HM-VEHOV",       "1/TRAFFIC/TRANSPORTATION ACCIDENTS - HIGH MECHANISM - VEHICLE OFF BRIDGE/HEIGHT",
      "TTA-HM-WC",          "1/TRAFFIC/TRANSPORTATION ACCIDENTS - HIGH MECHANISM - WATERCRAFT",
      "TTA-INJ",            "2/TRAFFIC/TRANSPORTATION ACCIDENTS - WITH INJURIES",
      "TTA-MI-AIR",         "1/TRAFFIC/TRANSPORTATION ACCIDENTS - MAJOR INCIDENT-AIRCRAFT",
      "TTA-MI-BUS",         "1/TRAFFIC/TRANSPORTATION ACCIDENTS - MAJOR INCIDENT-BUS",
      "TTA-MI-MV",          "1/TRAFFIC/TRANSPORTATION ACCIDENTS - MAJOR INCIDENT- MULTI-VEHICLE PILEUP",
      "TTA-MI-SUB",         "1/TRAFFIC/TRANSPORTATION ACCIDENTS - MAJOR INCIDENT-SUBWAY",
      "TTA-MI-TRAIN",       "1/TRAFFIC/TRANSPORTATION ACCIDENTS - MAJOR INCIDENT-TRAIN",
      "TTA-MI-WC",          "1/TRAFFIC/TRANSPORTATION ACCIDENTS - MAJOR INCIDENT-WATERCRAFT",
      "TTA-NA",             "1/TRAFFIC/TRANSPORTATION ACCIDENTS - NOT ALERT",
      "TTA-NOINJ",          "3/TRAFFIC/TRANSPORTATION ACCIDENTS - NO INJURIES (CONFIRMED)",
      "TTA-OH",             "2/TRAFFIC/TRANSPORTATION ACCIDENTS - OTHER HAZARDS",
      "TTA-PIN",            "1/TRAFFIC/TRANSPORTATION ACCIDENTS - PINNED (TRAPPED) VICTIM",
      "TTA-SH",             "2/TRAFFIC/TRANSPORTATION ACCIDENTS - SERIOUS HEMORRHAGE",
      "TTA-US",             "2/TRAFFIC/TRANSPORTATION ACCIDENTS - UNKNOWN STATUS",
      "UNC",                "2/UNCONSCIOUS/FAINTING (NEAR)",
      "UNC-AG/IB",          "1/UNCONSCIOUS/FAINTING (NEAR) - UNCONSCIOUS - AGONAL/INEFFECTIVE BREATHING",
      "UNC-ALERT",          "2/UNCONSCIOUS/FAINTING (NEAR) - ALERT WITH ABNORMAL BREATHING",
      "UNC-CC",             "1/UNCONSCIOUS/FAINTING (NEAR) - CHANGING COLOR",
      "UNC-EB",             "1/UNCONSCIOUS/FAINTING (NEAR) - UNCONSCIOUS - EFFECTIVE BREATHING",
      "UNC-F-ACH",          "2/UNCONSCIOUS/FAINTING (NEAR) - FAINTING EPISODE(S) AND ALERT O 35  CARD HX",
      "UNC-FNC",            "3/UNCONSCIOUS/FAINTING (NEAR) - FAINTING EPISODE(S) AND ALERT O 35 NO CARD HX",
      "UNC-FNCH",           "3/UNCONSCIOUS/FAINTING (NEAR) - FAINTING EPISODE(S) AND ALERT U 35 NO CARD HX",
      "UNC-FUCH",           "3/UNCONSCIOUS/FAINTING (NEAR) - FAINTING EPISODE(S) AND ALERT U 35   CARD HX",
      "UNC-FWAP",           "2/UNCONSCIOUS/FAINTING (NEAR) - FEMALES 12-50 WITH ABDOMINAL PAIN",
      "UNC-IB",             "1/UNCONSCIOUS/FAINTING (NEAR) - INEFFECTIVE BREATHING",
      "UNC-NA",             "1/UNCONSCIOUS/FAINTING (NEAR) - NOT ALERT",
      "UNK",                "1/UNKNOWN TROUBLE",
      "US",                 "1/UNKNOWN STATUS (MAN DOWN)",
      "US-LANG",            "2/UNKNOWN STATUS (MAN DOWN) - CALLERS LANGUAGE NOT UNDERSTOOD",
      "US-LSQ",             "1/UNKNOWN STATUS (MAN DOWN) - LIFE STATUS QUESTIONABLE",
      "US-MED",             "3/UNKNOWN STATUS (MAN DOWN) - MEDICAL ALARM",
      "US-STAND",           "3/UNKNOWN STATUS (MAN DOWN) - STANDING,SITTING,MOVING OR TALKING",
      "US-US",              "2/UNKNOWN STATUS (MAN DOWN) - UNKNOWN STATUS"
  });

  private static final Properties GPS_TABLE = buildCodeTable(new String[]{
      "@305",   "31.153174,-97.324602",
      "@306",   "31.166908,-97.319074",
      "@307",   "31.180577,-97.313935",
      "@308",   "31.192139,-97.309398",
      "@309",   "31.208031,-97.302668",
      "@310",   "31.221252,-97.295544",
      "@311",   "31.233926,-97.288221",
      "@312",   "31.243887,-97.282241",
      "@313",   "31.256592,-97.275016",
      "@314",   "31.272733,-97.265484",
      "@315",   "31.284852,-97.256454"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BART", "BARTLETT",
      "BELL", "BELL COUNTY",
      "BLTN", "BELTON",
      "BRGS", "BRIGGS",
      "BRNT", "BURNET",
      "BRTM", "BERTRAM",
      "BUCK", "BUCKHOLTS",
      "BVED", "BRUCEVILLE-EDDY",
      "CMRN", "CAMERAON",
      "COPP", "COPPERAS COVE",
      "CRYL", "CORYELL COUNTY",
      "FALL", "FALLS COUNTY",
      "FRNC", "FLORENCE",
      "FTHD", "FORT HOOD",
      "GRGR", "GRANGER",
      "GRGT", "GEORGETOWN",
      "HKRH", "HARKER HEIGHTS",
      "HLND", "HOLLAND",
      "JREL", "JARREL",
      "KEMP", "KEMPNER",
      "KILN", "KILLEEN",
      "LBHL", "LIBERTY HILL",
      "LMPS", "LAMPASAS COUNTY",
      "LOTT", "LOTT",
      "LRVR", "LITTLE RIVER",
      "MCLN", "MCLENNAN COUNTY",
      "MILM", "MILAM COUNTY",
      "MPRC", "MORGANS POINT RESORT",
      "NOLN", "NOLANVILLE",
      "OGBY", "OBLESBY",
      "ROGR", "ROGERS",
      "RSBD", "ROSEBUD",
      "SLDO", "SALADO",
      "SWNR", "SCHWERTNER",
      "TAYL", "TAYLOR",
      "THRL", "THRALL",
      "TMPL", "TEMPLE",
      "TRDL", "THORNDALE",
      "TROY", "TROY",
      "WLMN", "WILLIAMSON COUNTY"
  });

}
