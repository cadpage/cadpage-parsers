package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.StandardCodeTable;

/**
 * Onondaga County, NY
 */
public class NYOnondagaCountyAParser extends FieldProgramParser {

  public NYOnondagaCountyAParser() {
    super(CITY_CODES, "ONONDAGA COUNTY", "NY",
        "ADDR/SXa XTS:X! P:PRI Lev:SKIP X:INFO Disp:UNIT%");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "e9web1@ongov.net,E9Notify@ongov.net,messaging@emergencysmc.com,@notifyatonce.net,messaging@iamresponding.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override
      public boolean splitBlankIns() {
        return false;
      }

      @Override
      public boolean mixedMsgOrder() {
        return true;
      }

      @Override
      public int splitBreakLength() {
        return 130;
      }

      @Override
      public int splitBreakPad() {
        return 1;
      }
    };
  }

  private static final Pattern MARKER = Pattern
      .compile("^(?:(?:(?:I/)?CAD MSG )?([A-Z]+) +)?(\\d\\d:\\d\\d:\\d\\d) +");

  private static final Pattern CITY_EXP_PTN = Pattern
      .compile("\\b(TMA),[A-Z]\\b");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Some services split message into subject and message
    if (subject.startsWith("CAD MSG") || subject.startsWith("I/CAD MSG")) {
      body = subject + " " + body;
    } else if (subject.equals("Cicero Fire")) {
      body = "I/CAD MSG " + body.replace('\n', ',');
    }

    // Make sure pages starts with initial marker
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    data.strSource = getOptGroup(match.group(1));
    data.strTime = match.group(2);
    body = body.substring(match.end());

    // Drop line breaks and duplicate blanks
    body = body.replace('\n', ' ').replaceAll("  +", " ");
    body = CITY_EXP_PTN.matcher(body).replaceAll("$1");
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "SRC TIME " + super.getProgram();
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_COUNTY_PTN = Pattern
      .compile(" +(CAY|COR|MAD|OND|ONO|OSW)$");
  private static final Pattern ADDR_ZIP_PTN = Pattern.compile(" +(13\\d{3})$");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      // Some times there is a county field, and sometimes there is not. In any
      // case, we can ignore it and rely on the city code in front of it
      Matcher match = ADDR_COUNTY_PTN.matcher(field);
      if (match.find())
        field = field.substring(0, match.start());

      String sPlace = "";
      int pt = field.indexOf(':');
      if (pt >= 0) {
        sPlace = field.substring(pt + 1).trim();
        field = field.substring(0, pt).trim();
        sPlace = stripFieldStart(sPlace, "@");
      }
      String apt = "";
      pt = field.lastIndexOf(',');
      if (pt >= 0) {
        apt = field.substring(pt + 1).trim();
        field = field.substring(0, pt);
        match = X_APT_PTN.matcher(apt);
        if (match.matches()) {
          String tmp = match.group(1);
          if (tmp != null)
            apt = tmp;
        }
      }

      if (field.startsWith("/")) {
        data.strCity = convertCodes(field.substring(1).trim(), CITY_CODES);
      } else {
        super.parse(field, data);
        data.strApt = append(data.strApt, "-", apt);
        if (!sPlace.equals("EST"))
          data.strPlace = append(data.strPlace, " - ", sPlace);
        match = ADDR_ZIP_PTN.matcher(data.strAddress);
        if (match.find()) {
          if (data.strCity.length() == 0)
            data.strCity = match.group(1);
          data.strAddress = data.strAddress.substring(0, match.start());
        }
      }
    }
  }

  private static final Pattern EXPANDED_DASH_APT_PTN = Pattern
      .compile("\\b([A-Z]|\\d{1,4}) - ([A-Z]|\\d{1,4})\\b");
  private static final Pattern X_APT_PTN = Pattern.compile(
      "(?:APT|RM|ROOM|SUITE|LOT|#)[-:/# ]*(.*)|\\d+(?: *- *[A-Z0-9]+)?",
      Pattern.CASE_INSENSITIVE);
  private static final Pattern STD_CODE_PTN = Pattern.compile("(\\d\\d[A-E])[- ]*(\\d\\d[A-Za-z]?)");

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String cross = p.get(',');
      String code = p.getLastOptional(',');
      String place = p.get();
      if (code.length() == 0) abort();

      if (data.strAddress.length() == 0) {
        if (!place.startsWith("BETWEEN ") && !place.startsWith("BTWN")) {
          Result res = parseAddress(StartType.START_ADDR, FLAG_NO_CITY, place);
          if (res.getStatus() > STATUS_INTERSECTION) {
            res.getData(data);
            ;
            place = stripFieldStart(res.getLeft(), "-");
          }
        }
        if (data.strAddress.length() == 0) {
          parseAddress(cross, data);
          cross = "";
        }
      }
      super.parse(cross, data);

      place = EXPANDED_DASH_APT_PTN.matcher(place).replaceAll("$1-$2");
      for (String part :  place.split(" - ")) {
        part = part.trim();
        Matcher match = X_APT_PTN.matcher(part);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null)
            apt = part.replace(" ", "");
          if (apt.length() > 0) {
            if (apt.contains(data.strApt)) {
              data.strApt = apt;
            } else if (!data.strApt.contains(apt)) {
              data.strApt = append(data.strApt, "-", apt);
            }
          }
        } else {
          part = stripFieldStart(part, "IFO");
          data.strPlace = append(data.strPlace, " - ", part);
        }
      }

      Matcher match = STD_CODE_PTN.matcher(code);
      if (match.matches()) {
        code = match.group(1) + match.group(2);
      } else {
        code = stripFieldEnd(code, "-default");
        code = stripFieldEnd(code, "-");
      }
      data.strCode = code;

      String call = STD_CALL_CODES.getCodeDescription(code);
      if (call != null) {
        if (code.length() >= 3) {
          String priority = STD_CODE_PRI.getProperty(code.substring(2,3));
          if (priority != null) data.strPriority = priority;
        }
      } else {
        call = CALL_CODES.getProperty(code);
        if (call == null) {
          int pt = code.indexOf(' ');
          if (pt >= 0) call = CALL_CODES.getProperty(code.substring(0, pt));
        }
        if (call == null) {
          call = code;
        } else {
          int pt = call.indexOf(", PRIORITY ");
          if (pt >= 0) {
            data.strPriority = call.substring(pt + 11).trim();
            call = call.substring(0, pt).trim();
          }
        }
      }
      data.strCall = call;
    }

    @Override
    public String getFieldNames() {
      return "ADDR X APT PLACE CODE CALL PRI";
    }
  }

  // Remove redundant date/time from info field
  private static final Pattern INFO_MAP_GPS_PTN = Pattern
      .compile("(?:[A-Z]{1,2} SECTOR|CELL = \\d+ SECTOR = \\d+?) +([-+]?\\d{3}\\.\\d{6} [-+]?\\d{3}\\.\\d{6})\\b *");
  private static final Pattern INFO_DATE_TIME_PTN = Pattern
      .compile("\\b\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d\\b");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_MAP_GPS_PTN.matcher(field);
      if (match.lookingAt()) {
        setGPSLoc(match.group(1), data);
        field = field.substring(match.end());
      }

      match = INFO_DATE_TIME_PTN.matcher(field);
      if (match.find()) {
        field = append(field.substring(0, match.start()).trim(), " ", field
            .substring(match.end()).trim());
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "GPS INFO";
    }
  }

  @Override
  public String adjustMapAddress(String address) {
    address = ROUTE_11_PTN.matcher(address).replaceAll(
        "NEW YORK STATE BICYCLE ROUTE 11");
    address = ROUTE_20_PTN.matcher(address).replaceAll("US 20");
    return address;
  }

  private static final Pattern ROUTE_11_PTN = Pattern
      .compile("\\b(?:RT|ROUTE) *11\\b");
  private static final Pattern ROUTE_20_PTN = Pattern
      .compile("\\b(?:RT|ROUTE) *20\\b");

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("ONONDAGA NATION"))
      city = "ONONDAGA COUNTY";
    return city;
  }

  private static final CodeTable STD_CALL_CODES = new StandardCodeTable();

  private static final Properties STD_CODE_PRI = buildCodeTable(new String[] {
      "A", "3",
      "B", "2",
      "C", "2",
      "D", "1",
      "E", "1"
  });

  private static final Properties CALL_CODES = buildCodeTable(new String[] {

      // New Numeric codes
      "01A", "ABDOMINAL PAIN, PRIORITY 3",
      "01C", "ABDOMINAL PAIN, PRIORITY 2",
      "01D", "ABDOMINAL PAIN, PRIORITY 1",
      "02A", "ALLERGIES or ENVENOMATIONS, PRIORITY 3",
      "02B", "ALLERGIES or ENVENOMATIONS, PRIORITY 2",
      "02C", "ALLERGIES or ENVENOMATIONS, PRIORITY 2",
      "02D", "ALLERGIES or ENVENOMATIONS, PRIORITY 1",
      "02E", "ALLERGIES or ENVENOMATIONS, PRIORITY 1",
      "03A", "ANIMAL BITES / ATTACKS, PRIORITY 3",
      "03B", "ANIMAL BITES / ATTACKS, PRIORITY 2",
      "03D", "ANIMAL BITES / ATTACKS, PRIORITY 1",
      "04A", "ASSAULT (INJURIES), PRIORITY 3",
      "04B", "ASSAULT (INJURIES), PRIORITY 2",
      "04D", "ASSAULT (INJURIES), PRIORITY 1",
      "05A", "BACK PAIN, PRIORITY 3",
      "05C", "BACK PAIN, PRIORITY 2",
      "05D", "BACK PAIN, PRIORITY 1",
      "06C", "BREATHING PROBLEMS, PRIORITY 2",
      "06D", "BREATHING PROBLEMS, PRIORITY 1",
      "06E", "BREATHING PROBLEMS, PRIORITY 1",
      "07A", "BURNS, PRIORITY 3",
      "07B", "BURNS, PRIORITY 2",
      "07C", "BURNS, PRIORITY 2",
      "07D", "BURNS, PRIORITY 1",
      "07E", "BURNS, PRIORITY 1",
      "08B", "CARBON MONOXIDE / INHALATION / HAZMAT, PRIORITY 3",
      "08C", "CARBON MONOXIDE / INHALATION / HAZMAT, PRIORITY 2",
      "08D", "CARBON MONOXIDE / INHALATION / HAZMAT, PRIORITY 1",
      "09B", "CARDIAC or RESPIRATORY ARREST, PRIORITY 2",
      "09D", "CARDIAC or RESPIRATORY ARREST, PRIORITY 1",
      "09E", "CARDIAC or RESPIRATORY ARREST, PRIORITY 1",
      "10A", "CHEST PAINS, PRIORITY 3",
      "10C", "CHEST PAINS, PRIORITY 2",
      "10D", "CHEST PAINS, PRIORITY 1",
      "11A", "CHOKING, PRIORITY 3",
      "11D", "CHOKING, PRIORITY 1",
      "11E", "CHOKING, PRIORITY 1",
      "12A", "SEIZURES, PRIORITY 3",
      "12B", "SEIZURES, PRIORITY 2",
      "12C", "SEIZURES, PRIORITY 2",
      "12D", "SEIZURES, PRIORITY 1",
      "13A", "DIABETIC PROBLEM, PRIORITY 3",
      "13C", "DIABETIC PROBLEM, PRIORITY 2",
      "13D", "DIABETIC PROBLEM, PRIORITY 1",
      "14A", "DROWNING, PRIORITY 3",
      "14B", "DROWNING, PRIORITY 2",
      "14C", "DROWNING, PRIORITY 2",
      "14D", "DROWNING, PRIORITY 1",
      "14E", "DROWNING, PRIORITY 1",
      "15C", "ELECTROCUTION, PRIORITY 2",
      "15D", "ELECTROCUTION, PRIORITY 1",
      "15E", "ELECTROCUTION, PRIORITY 1",
      "16A", "EYE PROBLEM or INJURY, PRIORITY 3",
      "16B", "EYE PROBLEM or INJURY, PRIORITY 2",
      "16D", "EYE PROBLEM or INJURY, PRIORITY 1",
      "17A", "FALLS, PRIORITY 3",
      "17B", "FALLS, PRIORITY 2",
      "17D", "FALLS, PRIORITY 1",
      "18A", "HEADACHE, PRIORITY 3",
      "18B", "HEADACHE, PRIORITY 2",
      "18C", "HEADACHE, PRIORITY 2",
      "19A", "HEART PROBLEMS, PRIORITY 3",
      "19C", "HEART PROBLEMS, PRIORITY 2",
      "19D", "HEART PROBLEMS, PRIORITY 1",
      "20A", "HEAT or COLD EXPOSURE, PRIORITY 3",
      "20B", "HEAT or COLD EXPOSURE, PRIORITY 2",
      "20C", "HEAT or COLD EXPOSURE, PRIORITY 2",
      "20D", "HEAT or COLD EXPOSURE, PRIORITY 1",
      "21A", "HEMORRHAGE or LACERATION, PRIORITY 3",
      "21B", "HEMORRHAGE or LACERATION, PRIORITY 2",
      "21C", "HEMORRHAGE or LACERATION, PRIORITY 2",
      "21D", "HEMORRHAGE or LACERATION, PRIORITY 1",
      "22A", "INACCESSIBLE INCIDENT / OTHER ENTRAPMENTS, PRIORITY 3",
      "22B", "INACCESSIBLE INCIDENT / OTHER ENTRAPMENTS, PRIORITY 2",
      "22D", "INACCESSIBLE INCIDENT / OTHER ENTRAPMENTS, PRIORITY 1",
      "23B", "OVERDOSE or POISONING, PRIORITY 2",
      "23C", "OVERDOSE or POISONING, PRIORITY 2",
      "23D", "OVERDOSE or POISONING, PRIORITY 1",
      "24A", "MATERNITY, PRIORITY 3",
      "24B", "MATERNITY, PRIORITY 2",
      "24C", "MATERNITY, PRIORITY 2",
      "24D", "MATERNITY, PRIORITY 1",
      "25A", "PYSCHIATRIC / ABDNORMAL BEHAVIOR / SUICIDE ATTEMPT, PRIORITY 1",
      "25B", "PYSCHIATRIC / ABDNORMAL BEHAVIOR / SUICIDE ATTEMPT, PRIORITY 2",
      "25D", "PYSCHIATRIC / ABDNORMAL BEHAVIOR / SUICIDE ATTEMPT, PRIORITY 3",
      "26A", "SICK PERSON, PRIORITY 3",
      "26B", "SICK PERSON, PRIORITY 2",
      "26C", "SICK PERSON, PRIORITY 2",
      "26D", "SICK PERSON, PRIORITY 1",
      "27A", "STAB / GUNSHOT / PENETRATING TRAUMA, PRIORITY 1",
      "27B", "STAB / GUNSHOT / PENETRATING TRAUMA, PRIORITY 2",
      "27D", "STAB / GUNSHOT / PENETRATING TRAUMA, PRIORITY 3",
      "28A", "STROKE, PRIORITY 3",
      "28C", "STROKE, PRIORITY 2",
      "29A", "TRAFFIC / TRANSPORTATION INCIDENT, PRIORITY 1",
      "29B", "TRAFFIC / TRANSPORTATION INCIDENT, PRIORITY 2",
      "29D", "TRAFFIC / TRANSPORTATION INCIDENT, PRIORITY 3",
      "30A", "TRAUMATIC INJURY, PRIORITY 3",
      "30B", "TRAUMATIC INJURY, PRIORITY 2",
      "30D", "TRAUMATIC INJURY, PRIORITY 1",
      "31A", "UNCONSCIOUS PERSON, PRIORITY 3",
      "31C", "UNCONSCIOUS PERSON, PRIORITY 2",
      "31D", "UNCONSCIOUS PERSON, PRIORITY 1",
      "31E", "UNCONSCIOUS PERSON, PRIORITY 1",
      "32B", "UNKNOWN PROBLEM, PRIORITY 2",
      "32D", "UNKNOWN PROBLEM, PRIORITY 1",

      // Old alpha codes
      "ABC",      "ABC VIOLATION",
      "ABDT",     "ABDUCTION/KIDNAP",
      "AIR -C",   "AIRCRAFT - COMPLAINT",
      "AIR -E",   "AIRCRAFT - EMERGENCY/CRASH",
      "AIR",      "AIRCRAFT",
      "ALGR",     "ALLERGIC REACTION",
      "ALRM -B",  "ALARM - BURGLARY",
      "ALRM -C",  "ALARM - CARBON MONOXIDE",
      "ALRM -E",  "ALARM - ELEVATOR",
      "ALRM -F",  "ALARM - FIRE",
      "ALRM -M",  "ALARM - MEDICAL",
      "ALRM -P",  "ALARM - CALL FOR POLICE",
      "ALRM -R",  "ALARM - ROBBERY",
      "ALRM -U",  "ALARM - UNKNOWN TYPE",
      "ALRM -V",  "ALARM - VEHICLE",
      "ALRM",     "ALARM",
      "ALS",      "ALS CALL (COUNTY)",
      "ANML -A",  "ANIMAL COMPLAINT - VICIOUS, RABID, LOOSE, INJURED",
      "ANML -B",  "ANIMAL COMPLAINT - BARKING DOG",
      "ANML -I",  "ANIMAL COMPLAINT - BITE INJURY",
      "ANML",     "ANIMAL COMPLAINT",
      "ARSN",     "ARSON",
      "ASL -I",   "ASSAULT - EMS NOTIFIED",
      "ASL -T",   "ASSAULT",
      "ASL",      "ASSAULT",
      "AST -A",   "ASSIST - AMBULANCE",
      "AST -C",   "ASSIST - CITIZEN",
      "AST -CA",  "ASSIST - CITIZEN/AMBULANCE",
      "AST -F",   "ASSIST - FIRE",
      "AST -O",   "ASSIST - OTHER",
      "AST -P",   "ASSIST - POLICE",
      "AST -PA",  "ASSIST - POLICE/AMBULANCE",
      "AST",      "ASSIST",
      "BCAL",     "BUILDING COLLAPSE",
      "BCHK",     "BAD CHECK COMPLAINT",
      "BIO",      "BIOLOGICAL THREAT",
      "BLMJ",     "MAJOR BLEEDING",
      "BLS",      "BLS CALL (COUNTY)",
      "BOAT -D",  "BOAT COMPLAINT/ACCIDENT - DISABLED",
      "BOAT -E",  "BOAT COMPLAINT/ACCIDENT - EMERGENCY",
      "BOAT -F",  "BOAT COMPLAINT/ACCIDENT - FIRE",
      "BOAT -R",  "BOAT COMPLAINT/ACCIDENT - RECKLESS OPERATION",
      "BOAT",     "BOAT COMPLAINT/ACCIDENT",
      "BOMB",     "BOMB THREAT/DEVICE",
      "BRK",      "OUT OF SERVICE - BREAK",
      "BRNS",     "BURNS TO PERSON",
      "BURG",     "BURGLARY",
      "CALL -E",  "EMS CALL -  NATURE UNK",
      "CALL -F",  "FIRE CALL - NATURE UNK",
      "CALL -H",  "911 CALL - UNKNOWN",
      "CALL -P",  "POLICE CALL - NATURE UNK",
      "CALL",     "CALL",
      "CARD",     "CHEST PAIN-POSS HEART",
      "CHLD",     "CHILD ABUSE/NEGLECT",
      "CHOK",     "PERSON CHOKING",
      "CIVC",     "CIVILIAN COMPLAINT",
      "CIVL",     "CIVIL COMPLAINT (OCSO USE)",
      "CMIS",     "CRIMINAL MISCHIEF",
      "COP",      "COMMUNITY POLICE DETAIL",
      "CVA",      "POSSIBLE STROKE",
      "DECN",     "OUT OF SERVICE - DECONTAMINATION",
      "DEMO",     "DEMONSTRATION",
      "DET",      "OUT OF SERVICE - DETAIL",
      "DETL",     "DETAIL",
      "DIAB",     "DIABETIC EMERGENCY",
      "DIFB",     "DIFFICULTY BREATHING",
      "DISO",     "DISORIENTED PERSON",
      "DISP",     "CIVIL DISPUTE",
      "DIST",     "DISTURBANCE",
      "DOA",      "DEAD BODY",
      "DOM -I",   "DOMESTIC DISPUTE - EMS NTFD",
      "DOM -P",   "DOMESTIC DISPUTE - PHYSICAL",
      "DOM -V",   "DOMESTIC DISPUTE - VERBAL",
      "DOM -W",   "DOMESTIC DISPUTE - WEAPON",
      "DOM",      "DOMESTIC",
      "DOWN",     "PERSON DOWN",
      "DRUG",     "DRUG COMPLAINT",
      "DRWN",     "DROWNING",
      "DWI",      "DRIVING WHILE INTOXICATED",
      "ECSA",     "ESCAPE FROM CUSTODY",
      "ELEC",     "ELECTROCUTION",
      "ELEV",     "ELEVATOR MALFUNCTION",
      "ESCT -B",  "ESCORT - BLOOD",
      "ESCT -C",  "ESCORT - CUSTODY",
      "ESCT -F",  "ESCORT - FUNERAL",
      "ESCT -M",  "ESCORT - MONEY/BANK BAG",
      "ESCT -P",  "ESCORT - PERSONAL EFFECTS (MEDS/CLOTHING)",
      "ESCT",     "ESCORT",
      "EXPL",     "EXPLOSION",
      "FIGHT -I", "FIGHT - EMS NTFD",
      "FIGHT -T", "FIGHT",
      "FIGHT -W", "FIGHT W/WEAPON",
      "FIGHT",    "FIGHT",
      "FIRE -A",  "APPLIANCE FIRE",
      "FIRE -B",  "BARN FIRE",
      "FIRE -C",  "CHIMNEY FIRE",
      "FIRE -E",  "ELECTRICAL FIRE",
      "FIRE -F",  "FUME/ODOR INVESTIGATION",
      "FIRE -H",  "HI-RISE FIRE",
      "FIRE -I",  "INDUSTRIAL/COMMERCIAL FIRE",
      "FIRE -M",  "MULTIPLE OCCUPANCY/APARTMENT FIRE",
      "FIRE -O",  "OUTDOOR/BRUSH FIRE",
      "FIRE -P",  "UTILITY POLE FIRE",
      "FIRE -Q",  "FIRE INVESTIGATION",
      "FIRE -R",  "RESIDENTIAL FIRE",
      "FIRE -S",  "SCHOOL FIRE",
      "FIRE -T",  "TRANSIT - BUS/TRAIN FIRE",
      "FIRE -V",  "VEHICLE FIRE",
      "FIRE -W",  "WATER VESSEL/BOAT FIRE",
      "FIRE",     "FIRE",
      "FLUP",     "FOLLOW-UP INVESTIGATION",
      "FOOT",     "OUT OF SERVICE - WALKING BEAT",
      "FORG",     "FORGERY",
      "FRAD",     "FRAUD",
      "FWAR",     "WARRANT ARREST WITH DR",
      "FWKS",     "FIREWORKS COMPLAINT",
      "GAMB",     "GAMBLING COMPLAINT",
      "GILL",     "GENERAL ILLNESS",
      "HARR",     "HARASSMENT",
      "HAZD",     "HAZARDOUS CONDITION",
      "HAZM",     "HAZARDOUS MATERIALS",
      "HELP",     "EMERGENCY POLICE ASSIST",
      "HOMI",     "HOMICIDE",
      "HUNT",     "ILLEGAL HUNTING",
      "ICE",      "ICE RESCUE",
      "IDEN",     "IDENTITY THEFT",
      "INDA",     "INDUSTRIAL ACCIDENT",
      "INFO",     "POLICE INFORMATION",
      "INTX",     "INTOXICATED PERSON",
      "IOD",      "INJURY ON DUTY",
      "JUVS",     "JUVENILE COMPLAINT",
      "KPROW",    "PROWLER",
      "LARC",     "LARCENY",
      "LEWD",     "LEWDNESS (FLASHER)",
      "LOCK -R",  "LOCKOUT - RESIDENTIAL",
      "LOCK -V",  "LOCKOUT - VEHICLE",
      "LOCK",     "LOCKOUT",
      "LOCL",     "LOCAL LAW VIOLATION",
      "LOCO",     "TRAIN ACCIDENT",
      "LOIT",     "LOITERING",
      "LOUD",     "NOISE COMPLAINT",
      "LUNC",     "OUT OF SERVICE - LUNCH",
      "MAIL",     "MAIL-IN COMPLAINT",
      "MAIN",     "OUT OF SERVICE - VEHICLE MAINTENANCE",
      "MATR",     "MATERNITY/CHILDBIRTH",
      "MEDV",     "MEDEVAC",
      "MEGL",     "MEGANS LAW",
      "MENA",     "MENACING",
      "MENT",     "POSSIBLE EDP",
      "MISC",     "MISCELLANEIOUS COMPLAINT",
      "MISP -E",  "MISSING PERSON - ENDANGERED",
      "MISP -I",  "MISSING PERSON - INVESTIGATION",
      "MISP -J",  "MISSING PERSON - JUVENILE (UNDER 14)",
      "MISP -S",  "MISSING PERSON - SEARCH",
      "MISP",     "MISSING PERSON",
      "MUTL -R",  "MUTUAL AID - RESPONSE",
      "MUTL -S",  "MUTUAL AID - STANDBY",
      "MUTL",     "MUTUAL AID",
      "MVC -B",   "MOTOR VEHICLE COLLISION - BUS INVOLVED",
      "MVC -BB",  "MOTOR VEHICLE COLLISION - BUS INVOLVED",
      "MVC -C",   "MOTOR VEHICLE COLLISION - INJURIES & STRUCTURE INVOLVED",
      "MVC -CB",  "MOTOR VEHICLE COLLISION - INJURIES & STRUCTURE INVOLVED/BUS",
      "MVC -D",   "MOTOR VEHICLE COLLISION - DAMAGE ONLY",
      "MVC -DB",  "MOTOR VEHICLE COLLISION - DAMAGE ONLY/BUS",
      "MVC -H",   "MOTOR VEHICLE COLLISION - HIT & RUN WITH INJURIES",
      "MVC -HB",  "MOTOR VEHICLE COLLISION - HIT & RUN WITH INJURIES/BUS",
      "MVC -I",   "MOTOR VEHICLE COLLISION - INJURIES PRESENTED",
      "MVC -IB",  "MOTOR VEHICLE COLLISION - INJURIES PRESENTED/BUS",
      "MVC -L",   "MOTOR VEHICLE COLLISION - VEHICLE LEFT SCENE",
      "MVC -LB",  "MOTOR VEHICLE COLLISION - VEHICLE LEFT SCENE/BUS",
      "MVC -P",   "MOTOR VEHICLE COLLISION - POSSIBLE INJURIES",
      "MVC -PB",  "MOTOR VEHICLE COLLISION - POSSIBLE INJURIES/BUS INVOLVED",
      "MVC -S",   "MOTOR VEHICLE COLLISION - STRUCTURE INVOLVED",
      "MVC -SB",  "MOTOR VEHICLE COLLISION - STRUCTURE INVOLVED/BUS",
      "MVC",      "MOTOR VEHICLE COLLISION",
      "NOTI",     "NOTIFCATION",
      "OPEN",     "OPEN DOOR/WINDOW",
      "OUT",      "OUT OF SERVICE",
      "OVRD",     "OVERDOSE",
      "P3",       "NON-EMERG EMS TRANSPORT",
      "PAIN",     "PAIN (NON-TRAUMATIC)",
      "PCHK",     "PROPERTY CHECK",
      "PDAM",     "PROPERTY DAMAGE",
      "PINJ",     "PERSONAL INJURY",
      "POI",      "POINT OF INFORMATION",
      "POIS",     "POISONING",
      "PROP",     "LOST/FOUND PROPERTY",
      "PROS",     "PROSTITUTION COMPLAINT",
      "RAPE",     "RAPE",
      "REND",     "RECKLESS ENDANGERMENT",
      "ROB -B",   "ROBBERY",
      "ROB -I",   "ROBBERY - EMS NTFD",
      "ROB -IB",  "ROBBERY - EMS NTFD",
      "ROB",      "ROBBERY",
      "RUPT",     "RUPTURED PIPELINE",
      "S49",      "IMMEDIATE REQ FOR POLICE",
      "S99",      "WORKING STRUCTURE FIRE",
      "SEIZ",     "SEIZURES",
      "SHOI",     "SHOOTING - EMS NTFD",
      "SHOT",     "SHOT(S) FIRED",
      "SNOW",     "PLOWING COMPLAINT",
      "SP",       "SUBJECT PURSUIT",
      "SPIL",     "FUEL SPILL",
      "STAB",     "STABBING",
      "STAT",     "STATUS CHECK",
      "STOP",     "TRAFFIC STOP",
      "SUIC",     "SUICIDE - ATTEMPT/THREAT",
      "SUMM",     "SUMMONS SERVICE",
      "SUS -I",   "SUSPICIOUS INCIDENT",
      "SUS -P",   "SUSPICIOUS PERSON",
      "SUS -V",   "SUSPICIOUS VEHICLE",
      "SUS -W",   "SUSPICIOUS PERSON WITH WEAPON",
      "SUS",      "SUSPICIOUS",
      "SXOF",     "SEX OFFENSE",
      "TP",       "TRAFFIC PURSUIT",
      "TRAF",     "TRAFFIC COMPLAINT",
      "TRAN",     "TRANSPORT BY POLICE",
      "TRES",     "TRESPASSING",
      "TRNG",     "OUT OF SERVICE - TRAINING",
      "UNCP",     "UNCONSCIOUS PERSON",
      "VEH -A",   "VEHICLE COMPLAINT - ABANDONED",
      "VEH -D",   "VEHICLE COMPLAINT - DISABLED",
      "VEH -L",   "VEHICLE COMPLAINT - LOCATED FILE 1",
      "VEH -P",   "VEHICLE COMPLAINT - PARKING",
      "VEH -R",   "VEHICLE COMPLAINT - REPOSSESSED",
      "VEH -S",   "VEHICLE COMPLAINT - STOLEN",
      "VEH -T",   "VEHICLE COMPLAINT - TOWED",
      "VEH",      "VEHICLE COMPLAINT",
      "WALK",     "WALK-IN EMS TO STATION",
      "WARP",     "WARRANT PROCESSED",
      "WARR",     "WARRANT ARREST",
      "WIRE -E",  "WIRE(S) DOWN - EMERGENCY",
      "WIRE -U",  "WIRE(S) DOWN - UNKNOWN STATUS",
      "WIRE",     "WIRE(S) DOWN",
      "WTRP -H",  "WATER PROBLEM - HYDRANT",
      "WTRP -M",  "WATER PROBLEM - WATER MAIN/SERVICE PROBLEM",
      "WTRP -S",  "WATER PROBLEM - STRUCTURE INVOLVED",
      "WTRP",     "WATER PROBLEM",
      "XING",     "SCHOOL CROSSING DET"

  // Unidentified codes
  // 26C -01
  // 26C -02
  // AIR -A2
  // AIR -A2B
  // ALRM -Q
  // ASL -IA
  // AST -CC
  // AST -PC
  // DOM -IA
  // DOM -IB
  // DOM -VA
  // EXPO -C
  // EXPO -H
  // FGH -IA
  // FGH -IB
  // INDA -A
  // LOCO -A
  // STAB -A
  // STAT -A
  // SUIC -E
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[] {

      // Cahyuga County
      "OCAU", "AUBURN CITY",
      "OTAR", "AURELIUS",
      "OTBT", "BRUTUS",
      "OTCA", "CAYUGA",
      "OTCQ", "CONQUEST",
      "OTCT", "CATO",
      "OTFL", "FLEMING",
      "OTGN", "GENOA",
      "OTIR", "IRA",
      "OTLD", "LEDYARD",
      "OTLO", "LOCKE",
      "OTME", "MERIDIAN",
      "OTMN", "MONTEZUMA",
      "OTMO", "MORAVIA",
      "OTNI", "NILES",
      "OTOW", "OWASCO",
      "OTSM", "SUMMERHILL",
      "OTSN", "SEMPRONIUS",
      "OTSO", "SCIPIO",
      "OTSP", "SPRINGPORT",
      "OTSR", "STERLING",
      "OTTH", "THROOP",
      "OTVC", "VICTORY",
      "OTVN", "VENICE",
      "OVAU", "AURORA",
      "OVCT", "CATO",
      "OVFH", "FAIR HAVEN",
      "OVME", "MENTZ",    // or Meridian
      "OVMO", "MORAVIA",
      "OVPO", "PORT BYRON",
      "OVUS", "UNION SPRINGS",
      "OVWE", "WEEDSPORT",

      // Cortland County
      "OCCO", "CORTLAND CITY",
      "OTCN", "CINCINNATUS",
      "OTCO", "CORTLANDVILLE",
      "OTCU", "CUYLER",
      "OTFT", "FREETOWN",
      "OTHO", "HOMER",
      "OTHR", "HARFORD",
      "OTLP", "LAPEER",
      "OTMR", "MARATHON",
      "OTPB", "PREBLE",
      "OTSL", "SOLON",
      "OTST", "SCOTT",
      "OTTA", "TAYLOR",
      "OTTX", "TRUXTON",
      "OTVG", "VIRGIL",
      "OTWI", "WILLETT",
      "OVHO", "HOMER",
      "OVMC", "MCGRAW",
      "OVMR", "MARATHON",

      // Madison County
      "OCON", "ONEIDA CITY",
      "OTBK", "BROOKFIELD",
      "OTCZ", "CAZENOVIA",
      "OTDR", "DERUYTER TOWN",
      "OTEA", "EATON",
      "OTFE", "FENNER",
      "OTGE", "GEORGETOWN",
      "OTHM", "HAMILTON",
      "OTLB", "LEBANON",
      "OTLI", "LINCOLN",
      "OTLN", "LENOX",
      "OTMD", "MADISON",
      "OTNE", "NELSON",
      "OTSF", "SMITHFIELD",
      "OTSK", "STOCKBRIDGE",
      "OTSV", "SULLIVAN",
      "OVCH", "CHITTENANGO",
      "OVCN", "CANASTOTA",
      "OVCZ", "CAZENOVIA",
      "OVDR", "DERUYTER",
      "OVEA", "EARLVILLE",
      "OVHM", "HAMILTON",
      "OVMD", "MADISON",
      "OVMU", "MUNNSVILLE",
      "OVMV", "MORRISVILLE",
      "OVWM", "WAMPSVILLE",

      // Oneida County
      "OCRO", "ROME CITY",
      "OCSH", "SHERRILL CITY",
      "OCUT", "UTICA CITY",
      "OTAN", "AUBURN",   // or Annsville
      "OTAU", "AUGUSTA",
      "OTAV", "AVA",
      "OTBO", "BOONVILLE",
      "OTBR", "BRIDGEWATER",
      "OTCM", "CAMDEN",
      "OTDE", "DERUYTER",  // or Deerfield
      "OTFO", "FORESTPORT",
      "OTFR", "FLORENCE",
      "OTFY", "FLOYD",
      "OTKI", "KIRKLAND ",
      "OTLE", "LEE",
      "OTML", "MARSHALL",
      "OTMY", "MARCY",
      "OTNH", "NEW HARTFORD",
      "OTPS", "PARIS",
      "OTRE", "REMSEN",
      "OTSA", "SANGERFIELD",
      "OTSE", "SKANEATELES",

      // Was Steuben
      "OTTR", "TRENTON",
      "OTVE", "VERNON",
      "OTVI", "VIENNA",
      "OTVR", "VERONA",
      "OTWE", "WESTMORELAND",
      "OTWH", "WHITESTOWN",
      "OTWS", "WESTERN",
      "OVBA", "BARNEVELD",
      "OVBO", "BOONVILLE",
      "OVBR", "BRIDGEWATER",
      "OVCA", "CAMDEN",
      "OVCI", "CLINTON",
      "OVCY", "CLAYVILLE",
      "OVHP", "HOLLAND PATENT",
      "OVNH", "NEW HARTFORD",
      "OVNY", "NEW YORK MILLS",
      "OVOC", "ONEIDA CASTLE",
      "OVOF", "ORISKANY FALLS",
      "OVOR", "ORISKANY",
      "OVPR", "PREBLE",   // or PROSPECT
      "OVRE", "REMSEN",
      "OVSY", "SYLVAN BEACH",
      "OVVE", "VERNON",
      "OVWA", "WATERVILLE",
      "OVWH", "WHITESBORO",
      "OVYO", "YORKVILLE",

      // Onondaga County
      "NAT", "ONONDAGA NATION",
      "SYR", "SYRACUSE CITY",
      "TCI", "CICERO",
      "TCL", "CLAY",
      "TCM", "CAMILLUS",
      "TDW", "DEWITT",
      "TEB", "ELBRIDGE",
      "TFB", "FABIUS",
      "TGD", "GEDDES",
      "TLF", "LAFAYETTE",
      "TLY", "LYSANDER",
      "TMA", "MANLIUS",
      "TMR", "MARCELLUS",
      "TON", "ONONDAGA",
      "TOT", "OTISCO",
      "TPO", "POMPEY",
      "TSK", "SKANEATELES",
      "TSL", "SALINA",
      "TSP", "SPAFFORD",
      "TTU", "TULLY",
      "TVB", "VANBUREN",
      "VBV", "BALDWINSVILLE",
      "VCM", "CAMILLUS",
      "VEB", "ELBRIDGE",
      "VES", "EAST SYRACUSE",
      "VFB", "FABIUS",
      "VFY", "FAYETTEVILLE",
      "VJR", "JORDAN",
      "VLV", "LIVERPOOL",
      "VMA", "MANLIUS",
      "VMI", "MINOA",
      "VMR", "MARCELLUS",
      "VNS", "NORTH SYRACUSE",
      "VSK", "SKANEATELES",
      "VSV", "SOLVAY",
      "VTU", "TULLY",

      // Oswego County
      "OCFU", "FULTON CITY",
      "OCOS", "OSWEGO CITY",
      "OTAL", "ALBION",
      "OTAM", "AMBOY",
      "OTBY", "BOYLSTON",
      "OTCS", "CONSTANTIA",
      "OTGR", "GRANBY",
      "OTHN", "HANNIBAL",
      "OTHS", "HASTINGS",
      "OTHV", "NEW HAVEN",
      "OTMI", "MINETTO",
      "OTMX", "MEXICO",
      "OTOR", "ORWELL",
      "OTOS", "OSWEGO",
      "OTPA", "PARISH",
      "OTPL", "PALERMO",
      "OTRF", "REDFIELD",
      "OTRI", "RICHLAND",
      "OTSB", "SCRIBA",
      "OTSC", "SANDY CREEK",
      "OTSH", "SCHROEPPEL",
      "OTVL", "VOLNEY",
      "OTWM", "WEST MONROE",
      "OTWT", "WILLIAMSTOWN",
      "OVAT", "ALTMAR",
      "OVCL", "CLEVELAND",
      "OVCS", "CENTRAL SQUARE",
      "OVHN", "HANNIBAL",
      "OVLA", "LACONA",
      "OVMX", "MEXICO",
      "OVPA", "PARISH",
      "OVPF", "PHOENIX",
      "OVPU", "PULASKI",
      "OVSC", "SANDY CREEK"
  });

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "STRAWBERRY LN & SALT SPRINGS RD",         "43.027955,-75.954269",
  });

}
