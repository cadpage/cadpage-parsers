package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Bucks County, PA
 */
public class PABucksCountyAParser extends PABucksCountyBaseParser {

  public PABucksCountyAParser() {
    super("SRC type:CALL! Box:BOX? adr:ADDR! aai:AAI box:BOX map:MAP tm:TIME% TEXT:INFO? Run:UNIT");
    setupSpecialStreets("CUL DE SAC");
    setupCities(CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "8276,@bnn.us,iamresponding.com,Bucks RSAN,@alert.bucksema.org,1210,@co.bucks.pa.us,@buckscounty.org,@everbridge.net,@buckscounty.gov";
  }

  private static final Pattern STATION_PTN = Pattern.compile("Station +(.*)");
  private static final Pattern TRAIL_URL_PTN = Pattern.compile("(.*)\\. {5,}(https:[^ ]+) {3,}.*");
  private static final Pattern MARKER1 = Pattern.compile("(?:(Station [^/:]+) / )?([A-Z]+\\s+(?:Adr:|adr:|Box:).*)", Pattern.DOTALL);
  private static final Pattern MARKER2 = Pattern.compile("^([A-Z0-9 ]+): *([A-Z]+) *");
  private static final Pattern NAKED_DATE_TIME = Pattern.compile("(?<!: ?)\\d\\d/\\d\\d/\\d\\d +\\d\\d:\\d\\d:\\d\\d\\b");
  private static final Pattern GEN_ALERT_MARKER = Pattern.compile("^(\\d\\d/\\d\\d/\\d\\d) +(\\d\\d:\\d\\d:\\d\\d) +~TO~ [A-Z0-9]+ FROM [A-Z0-9]+:\n?");
  private static final Pattern SRC_MARKER = Pattern.compile("^([A-Z]+[0-9]+)[, ]");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    body = stripFieldStart(body, "911:");
    body = stripFieldStart(body, "Text:");

    body = stripFieldEnd(body, "=");
    int pt = body.lastIndexOf('=');
    if (pt >= 100) body = body.substring(0,pt) + body.substring(pt+1);
    pt = body.indexOf("\n*****");
    if (pt < 0) pt = body.indexOf("Sent by mss911 ");
    if (pt < 0) pt = body.indexOf("\nSent by Berks County RSAN");
    if (pt >= 0) body = body.substring(0,pt).trim();
    else {
      Matcher match = TRAIL_URL_PTN.matcher(body);
      if (match.matches()) {
        body = match.group(1).trim();
        data.strInfoURL = match.group(2);
      }
    }
    String saveBody = body;

    Matcher match = STATION_PTN.matcher(subject);
    if (match.matches()) {
      data.strSource = match.group(1).trim();
      body = body.replace("\nBox:", " Box:").replace("\nAddr:", " adr:").replace("\nBtwn:", " btwn:").replace("\nText:", " Text:").replace('\n', ' ');
    }

    boolean mark2 = false;
    match = MARKER1.matcher(body);
    if (match.matches()) {
      String src = match.group(1);
      body = "type:" + match.group(2).replace('\n', ' ').trim();
      if (src != null) body = src + " " + body;
    } else {
      match = MARKER2.matcher(body);
      if (match.find()) {
        mark2 = true;
        body = match.group(1) + " type:" + match.group(2) + " " + body.substring(match.end()).replace('\n', ' ').trim();
      }
    }

    body = NAKED_DATE_TIME.matcher(body).replaceFirst(" tm: $0");
    body = body.replace(" Adr:", " adr:").replace(" stype:", " type:").replace(" saai:", " aai:").replace(" Text:", " aai:").trim();
    if (super.parseMsg(body, data)) {
      if (mark2 && data.strUnit.length() == 0) {
        data.strUnit = data.strSource;
        data.strSource = "";
      }
      return true;
    }

    // Parse failure - but see if this is one of two kinds of recognized general message
    if (subject.equals("Important message from Bucks County RSAN")) {
      data.parseGeneralAlert(this, saveBody);
    } else if (subject.equals("911 Data") || subject.equals("CAD Incident")) {
      match = GEN_ALERT_MARKER.matcher(saveBody);
      if (!match.find()) return false;
      data.parseGeneralAlert(this, saveBody.substring(match.end()).trim());
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    } else return false;

    //  See if the general alert data (in the place field) has a leading station code
    setFieldList("DATE TIME SRC INFO");
    match = SRC_MARKER.matcher(data.strSupp);
    if (match.find()) {
      data.strSource = match.group(1);
      data.strSupp = data.strSupp.substring(match.end()).trim();
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC UNIT " + super.getProgram() + " URL";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("AAI")) return new MyAaiField();
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {

    @Override
    public void parse(String field, Data data) {
      String desc = TYPE_CODES.getProperty(field);
      if (desc != null) field = field + " - " + desc;
      super.parse(field, data);
    }
  }

  private static final Pattern MULT_COMMA_PTN = Pattern.compile(",,+");
//   private static final Pattern BAD_CITY_PTN = Pattern.compile(".*[-\\*].*|.*? .*? .*? .*? .*");
  private static final Pattern CROSS_MARK_PTN = Pattern.compile("\\b(XSTRT:|XCROS:|low xst:|XSTS?[,: ]|X STR )", Pattern.CASE_INSENSITIVE);
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=#)");
  private static final Pattern PLACE_ADDR_PTN = Pattern.compile("([^,]+),([^,]+)");
  private static final Pattern PHONE_PTN = Pattern.compile("(.*?)[- #;,'/]+(\\d{3}[-. ]?\\d{3}[-. ]?\\d{4}|NO PHONE NUMBER)\\b(?: RESD?[:= ]| */)? *(.*)");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|UNIT|RM|LOT|SUITE) +(.*)");
  private static final Pattern LEAD_RES_PTN = Pattern.compile("RESD?[:=; ]+(.*)");
  private static final Pattern TRAIL_RES_PTN = Pattern.compile("(.*) RESD?");
  private static final Pattern KEEP_CROSS_PTN = Pattern.compile("&.*|BY|EXIT.*|ON .*|CROSSING");
  private static final Pattern TRAIL_DIR_PTN = Pattern.compile("(.*) ([NSEW]|[NS][EW])");

  private class MyAddressField extends AddressField {

    @Override
    public void parse(String sAddr, Data data) {

      // Not the usual parseAddress method, this one is in the PABucksCountyBaseParser class
      sAddr = MULT_COMMA_PTN.matcher(sAddr).replaceAll(",");
      sAddr = CROSS_MARK_PTN.matcher(sAddr).replaceFirst("btwn:");
      sAddr = MISSING_BLANK_PTN.matcher(sAddr).replaceAll(" ");
      parseAddressA7(sAddr, data);

      if (!data.strCity.isEmpty() && data.strCross.isEmpty() && !data.strCity.contains(",")) {

        String city = data.strCity;
        data.strCity = "";
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
        if (!data.strCity.isEmpty()) {
          data.strCross = getLeft();
        } else {
          parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, city, data);
          if (!data.strCity.isEmpty()) {
            data.strCross = getStart();
          } else {
            data.strCity = city;
          }
        }
      }

      int pt = data.strCity.indexOf(',');
      if (pt >= 0) {
        data.strState = data.strCity.substring(pt+1);
        data.strCity = data.strCity.substring(0,pt);
      }

      Matcher match = PLACE_ADDR_PTN.matcher(data.strAddress);
      if (match.matches()) {
        data.strPlace = append(match.group(1).trim(), " - ", data.strPlace);
        data.strAddress = match.group(2).trim();
      }

      match = PHONE_PTN.matcher(data.strCross);
      if (match.matches()) {
        data.strCross =  match.group(1).trim();
        data.strPhone = match.group(2);
        data.strName = match.group(3);
      }

      String cross = data.strCross;
      data.strCross = "";
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, cross, data);
      String left = getLeft();
      if (KEEP_CROSS_PTN.matcher(left).matches()) {
        data.strCross = append(data.strCross, " ", left);
      } else if ((match = APT_PTN.matcher(left)).matches()) {
        left = match.group(1);
        if (!left.equals(data.strApt)) data.strApt = append(data.strApt, "-", left);
      } else if (NUMERIC.matcher(left).matches()) {
        if (!left.equals(data.strApt)) data.strApt = append(data.strApt, "-", left);
      } else if (data.strName.length() == 0 && data.strPhone.length() > 0){
        match = LEAD_RES_PTN.matcher(left);
        if (match.matches()) left =  match.group(1).trim();
        else {
          match = TRAIL_RES_PTN.matcher(left);
          if (match.matches()) left =  match.group(1).trim();
        }
        data.strName = left;
      } else {
        if (left.equals("SECTOR")) {
          match = TRAIL_DIR_PTN.matcher(data.strCross);
          if (match.matches()) {
            data.strCross = match.group(1).trim();
            left = match.group(2)+' ' + left;
          }
        }
        if (data.strCross.endsWith(" BUS") && left.startsWith("STOP")) {
          data.strCross = data.strCross.substring(0, data.strCross.length()-4).trim();
          left = "BUS " + left;
        }
        left = stripFieldStart(left, "@");
        data.strPlace = append(data.strPlace, " - ", left);
      }
      if (data.strPhone.equals("NO PHONE NUMBER")) data.strPhone = "";
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames() + " CITY ST X PHONE NAME INFO";
    }
  }

  private static final Pattern COMMA_PTN = Pattern.compile(" *,[ ,]*");
  private static final Pattern DASH_PTN = Pattern.compile(" *- *");
  private static final Pattern XSTR_PTN = Pattern.compile("(?:XST(?:R[ST]?)?|XST|XCROSS|XSTREET|X)[-: ]+(.*)");
  private static final Pattern COVER_PTN = Pattern.compile("\\bCV +[A-Z]?(\\d)\\d\\d[A-Z]?\\b");
  private class MyAaiField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      // If the address does not look like an address, we may want to replace it
      // with something in this field.
      boolean checkAddress = data.strPlace.length() == 0 &&
                             data.strCity.length() == 0 &&
                             !isValidAddress(data.strAddress);

      // Look for GPS coordinates
      field = setGPSLoc(field, data);

      String result = "";
      for (String part : COMMA_PTN.split(field)) {

        if (part.startsWith("BOX ")) {
          data.strBox = part.substring(4).trim();
          continue;
        }

        Matcher match = XSTR_PTN.matcher(part);
        if (match.matches()) {
          data.strCross = append(data.strCross, " / ", match.group(1));
          continue;
        }

        if (data.strCity.length() == 0) {
          // here we will check for a dash separator :(
          String city = null;
          String[] pts = DASH_PTN.split(part);
          if (pts.length == 2) {
            city = getOutsideCity(pts[0]);
            if (city != null) {
              part = pts[1];
            } else {
              city = getOutsideCity(pts[1]);
              if (city != null) part = pts[0];
            }
          } else {
            city = getOutsideCity(part);
            if (city != null) part = null;
          }
          if (city != null) {
            int pt = city.indexOf(',');
            if (pt >= 0) {
              data.strState = city.substring(pt+1).trim();
              city = city.substring(0,pt).trim();
            }
            data.strCity = city;
            if (part == null) continue;
          }
        }

        if (part.equals("NY") || part.equals("NJ")) {
          data.strState = part;
          continue;
        }

        if (checkAddress && isValidAddress(part)) {
          data.strPlace = data.strAddress;
          data.strAddress = "";
          parseAddress(part, data);
          continue;
        }

        match = COVER_PTN.matcher(field);
        if (match.find()) {
          String code = match.group(1);
          switch (code.charAt(0)-'0') {
          case 3:
            data.strCity = "MONTGOMERY COUNTY";
            break;
          }
        }

        result = append(result, ", ", part);
      }
      super.parse(result, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE BOX ADDR CITY ST X GPS INFO";
    }
  }

  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }

  private class MyTimeField extends TimeField {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String token = p.get(' ');
      if (token.contains("/")) {
        data.strDate = token;
        token = p.get(' ');
      }
      data.strTime = token;
      data.strCallId = p.get();
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME ID";
    }
  }

  private static final Pattern LEAD_COMMA_PTN = Pattern.compile("^[, ]+");
  private static final Pattern TRAIL_COMMA_PTN = Pattern.compile("[, ]+$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = setGPSLoc(field, data);
      field = LEAD_COMMA_PTN.matcher(field).replaceFirst("");
      field = TRAIL_COMMA_PTN.matcher(field).replaceFirst("");
      field = COMMA_PTN.matcher(field).replaceAll(", ");
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "GPS INFO";
    }
  }

  private static final Properties TYPE_CODES = buildCodeTable(new String[]{
      "AIRC",     "AIRPLANE CRASH (TAC)",
      "ACARDA",   "CARDIAC/RESPIRATORY ARREST",
      "APOSXP",   "POSSIBLE EXPIRATION",
      "BOMB",     "BOMB THREAT",
      "BURN",     "BURN VICTIM (LOC)",
      "CBURN",    "CONTROLLED BURN",
      "COALRM",   "CARBON MONOXIDE ALARM (LOC)",
      "DECON",    "DECON ASSIGNMENT (LOC)",
      "ELEVAT",   "STUCK ELEVATOR (LOC)",
      "EXBLD",    "EXTINGUISHED STRUCTURE (TAC)",
      "FALRM",    "FIRE ALARM (LOC)",
      "FAPL",     "APPLIANCE FIRE (TAC)",
      "FAPT",     "APARTMENT FIRE (BOX)",
      "FAST",     "FIRE ASSIST (OUT OF COUNTY)",
      "FAUTO",    "AUTOMOBILE (LOC)",
      "FBARN",    "BARN FIRE (TAC)",
      "FBLD",     "BUILDING FIRE (BOX)",
      "FBOAT",    "BOAT FIRE (LOC)",
      "FBRU",     "BRUSH FIRE (LOC)",
      "FBUS",     "BUS FIRE (LOC)",
      "FCHIMN",   "CHIMNEY FIRE (TAC)",
      "FCOV",     "FIRE STATION COVER",
      "FDRILL",   "FIRE DRILL",
      "FDUMP",    "DUMPSTER FIRE (LOC)",
      "FDWL",     "DWELLING FIRE (TAC)",
      "FEXP",     "EXPLOSION (LOC)",
      "FEMS",     "FIRE ASSIST EMS (LOC)",
      "FEMSA",    "FIRE ASSIST EMS ALS",
      "FEMSB",    "FIRE ASSIST EMS BLS",
      "FGARAG",   "GARAGE FIRE (TAC)",
      "FGRILL",   "GRILL FIRE (LOC)",
      "FHAZ",     "HAZMAT FIXED LOCATION (TAC)",
      "FHOSP",    "HOSPITAL FIRE (BOX)",
      "FINFO",    "FIRE INFORMATION",
      "FINV",     "FIRE INVESTIGATION (LOC)",
      "FIRE",     "FIRE OTHERS (LOC)",
      "FIRCAL",   "FIRE CALL (EMS)",
      "FMPAGE",   "FM PAGE REQUEST",
      "FNURS",    "NURSING HOME FIRE (BOX)",
      "FOBLD",    "OUTBUILDING FIRE (TAC)",
      "FPAGE",    "FD PAGE REQUEST",
      "FPOL",     "FIRE POLICE REQUEST",
      "FRUB",     "RUBBISH FIRE (LOC)",
      "FSCHOL",   "SCHOOL FIRE (BOX)",
      "FSPEC",    "SPECIAL ASSIGNMENT",
      "FSPILL",   "FUEL LEAK INCIDENT (LOC)",
      "FSTORE",   "SMALL STORE FIRE (TAC)",
      "FTA",      "ACCIDENT W/FIRE (LOC)",
      "FTAI",     "ACCIDENT W/FIRE & INJ (LOC)",
      "FTHAZ",    "TRANSPORTATION HAZMAT",
      "FTRAIN",   "TRAIN FIRE (TAC)",
      "FTRUCK",   "TRUCK FIRE (LOC)",
      "FUMEIN",   "FUMES INSIDE STRUCTURE (TAC)",
      "FUMES",    "FUMES OUTSIDE (LOC)",
      "FUNK",     "UNKNOWN TYPE FIRE (LOC)",
      "GALRM",    "GENERAL ALARM",
      "GASL",     "GAS LEAK",
      "RAUTO",    "AUTO EXTRICATION (RBOX)",
      "RCOV",     "COVER (OUT OF COUNTY)",
      "RDOM",     "DOMESTIC RESCUE (RBOX)",
      "RMAR",     "MARINE RESCUE (MBOX)",
      "RVBLD",    "VEHICLE INTO A BUILDING (RBOX)",
      "STDBY",    "FIRE STANDBY",
      "WATER",    "WATER FLOW (LOC)",
      "WIREIN",   "WIRES IN DWELLING (TAC)",
      "WIRES",    "WIRES DOWN (LOC)",

      "AABDO",      "ACUTE ABDOMEN",
      "AALLR",      "ANAPHYLAXIS",
      "AASSL",      "ASSAULT VICTIM (ALS)",
      "ABLED",      "ACUTE HEMORRHAGE",
      "ABURN",      "CRITICAL BURN VICTIM",
      "ACHESP",     "CHEST PAINS (CARDIAC SYMPT.)",
      "ACHOKE",     "OBSTRUCTED AIRWAY (ACTIVE)",
      "ACOP",       "CARBON MONOXIDE POISONING",
      "ACVA",       "CVA/STROKE",
      "ADIAB",      "DIABETIC EMERGENCY",
      "ADROWN",     "DROWNING - CRITICAL",
      "AELEC",      "ELECTRICAL SHOCK VICTIM",
      "AENVIR",     "ENVIRONMENTAL EMERGENCY",
      "AFAINT",     "SYNCOPAL EPISODE",
      "AFALL",      "FALL VICTIM - CRITICAL",
      "AGUN",       "GUNSHOT VICTIM",
      "AHRI",       "ALS HIT & RUN ACCIDENT WITH INJURY",
      "AOBG",       "ALS OB/GYN EMERGENCY",
      "AOD",        "OVERDOSE/POISONING - CRITICAL",
      "APREG",      "MATERNITY - CRITICAL",
      "ARESP",      "RESPIRATORY DISTRESS",
      "ASEIZ",      "SEIZURES - ACTIVE",
      "AASSLT",     "ASSAULT W/ TRAUMA",
      "ASTAB",      "STABBING VICTIM",
      "ATAI",       "ALS TRAFFIC ACCIDENT INJURIES",
      "ATRAN",      "ALS/UNSPECIFIED EMERGENCY",
      "ATRAUM",     "MULTI-SYSTEMS TRAUMA",
      "AUNC",       "UNCONSCIOUS SUBJECT",
      "AUNR",       "UNRESPONSIVE SUBJECT",
      "BABDO",      "ABDOMINAL PAIN",
      "BALLR",      "ALLERGIC REACTION",
      "BASSLT",     "ASSAULT - NON CRITICAL",
      "BBLED",      "BLEEDING (NON TRAUMATIC)",
      "BCHESP",     "CHEST PAIN  (NON CRITICAL)",
      "BINJ",       "MINOR INJURY - NON CRITICAL",
      "BLAC",       "ANIMAL BITE",
      "BPSYCH",     "PSYCHIATRIC TRANSPORTATION",
      "BOD",        "OVERDOSE/POISONING - NON CRITICAL",
      "BPREG",      "MATERNITY - NON CRITICAL",
      "BUNK",       "BLS/UNKNOWN PROBLEM",
      "BBURN",      "SCALDING/MINOR BURNS",
      "BDROWN",     "AQUATIC ACCIDENT (NON CRITICAL)",
      "BFALL",      "FALL VICTIM - NON CRITICAL",
      "BLAC",       "LACERATION/CONTROLLED BLEEDING",
      "BPTAST",     "PATIENT ASSIST",
      "COP",        "CARBON MONOXIDE POISONING",
      "BTAI",       "BLS TRAFFIC ACCIDENT (MINOR INJURIES)",
      "BTRAN",      "EMERGENCY TRANSPORTATION",
      "ESPEC",      "STANDBY / SPECIAL ASSIGNMENT (EMS)",
      "MALRM",      "MEDICAL ALARM"
  });

  private static final String[] CITY_LIST = new String[] {

      // Boroughs
      "BRISTOL",
      "CHALFONT",
      "DOYLESTOWN",
      "DUBLIN",
      "HULMEVILLE",
      "IVYLAND",
      "LANGHORNE",
      "LANGHORNE MANOR",
      "MORRISVILLE",
      "NEW BRITAIN",
      "NEW HOPE",
      "NEWTOWN",
      "PENNDEL",
      "PERKASIE",
      "QUAKERTOWN",
      "RICHLANDTOWN",
      "RIEGELSVILLE",
      "SELLERSVILLE",
      "SILVERDALE",
      "TELFORD",
      "TRUMBAUERSVILLE",
      "TULLYTOWN",
      "YARDLEY",

      // Townships
      "BEDMINSTER TWP",
      "BEDMINSTER",
      "BENSALEM TWP",
      "BENSALEM",
      "BRIDGETON TWP",
      "BRIDGETON",
      "BRISTOL TWP",
      "BRISTOL",
      "BUCKINGHAM TWP",
      "BUCKINGHAM",
      "DOYLESTOWN TWP",
      "DOYLESTOWN",
      "DURHAM TWP",
      "DURHAM",
      "EAST ROCKHILL TWP",
      "EAST ROCKHILL",
      "FALLS TWP",
      "FALLS",
      "HAYCOCK TWP",
      "HAYCOCK",
      "HILLTOWN TWP",
      "HILLTOWN",
      "LOWER MAKEFIELD TWP",
      "LOWER MAKEFIELD",
      "LOWER SOUTHAMPTON TWP",
      "LOWER SOUTHAMPTON",
      "MIDDLETOWN TWP",
      "MIDDLETOWN",
      "MILFORD TWP",
      "MILFORD",
      "NEW BRITAIN TWP",
      "NEW BRITAIN",
      "NEWTOWN TWP",
      "NEWTOWN",
      "NOCKAMIXON TWP",
      "NOCKAMIXON",
      "NORTHAMPTON TWP",
      "NORTHAMPTON",
      "PLUMSTEAD TWP",
      "PLUMSTEAD",
      "RICHLAND TWP",
      "RICHLAND",
      "SOLEBURY TWP",
      "SOLEBURY",
      "SPRINGFIELD TWP",
      "SPRINGFIELD",
      "TINICUM TWP",
      "TINICUM",
      "UPPER MAKEFIELD TWP",
      "UPPER MAKEFIELD",
      "UPPER SOUTHAMPTON TWP",
      "UPPER SOUTHAMPTON",
      "WARMINSTER TWP",
      "WARMINSTER",
      "WARRINGTON TWP",
      "WARRINGTON",
      "WARWICK TWP",
      "WARWICK",
      "WEST ROCKHILL TWP",
      "WEST ROCKHILL",
      "WRIGHTSTOWN TWP",
      "WRIGHTSTOWN",

  // Census-designated places
      "BRITTANY FARMS-THE HIGHLANDS",
      "CHURCHVILLE",
      "CORNWELLS HEIGHTS",
      "CROYDON",
      "EDDINGTON",
      "FAIRLESS HILLS",
      "FEASTERVILLE",
      "LEVITTOWN",
      "MILFORD SQUARE",
      "NEWTOWN GRANT",
      "PLUMSTEADVILLE",
      "RICHBORO",
      "SPINNERSTOWN",
      "TREVOSE",
      "VILLAGE SHIRES",
      "WARMINSTER HEIGHTS",
      "WOODBOURNE",
      "WOODSIDE",

  // Unincorporated communities
      "ALMONT",
      "ANDALUSIA",
      "APPLEBACHSVILLE",
      "AQUETONG",
      "ARGUS",
      "BEDMINSTER",
      "BLOOMING GLEN",
      "BRICK TAVERN",
      "BRYN GWELED",
      "BUCKINGHAM",
      "BUCKSVILLE",
      "CALIFORNIA",
      "CARVERSVILLE",
      "CENTER BRIDGE",
      "DANBORO",
      "DOLINGTON",
      "DURHAM",
      "ELEPHANT",
      "ERWINNA",
      "EUREKA",
      "FALLSINGTON",
      "FERNDALE",
      "FINLAND",
      "FOREST GROVE",
      "FOUNTAINVILLE",
      "FURLONG",
      "GALLOWS HILL",
      "GARDENVILLE",
      "GERYVILLE",
      "HAGERSVILLE",
      "HARRIMAN",
      "HARROW",
      "HARTSVILLE",
      "HIGHLAND PARK",
      "HIGHTON",
      "HILLSIDE VILLAGE",
      "HILLTOP",
      "HILLTOWN",
      "HINKLETOWN",
      "HOLICONG",
      "HOOD",
      "JAMISON",
      "JOHNSVILLE",
      "KINTNERSVILLE",
      "KULPS CORNER",
      "LAHASKA",
      "LINE LEXINGTON",
      "LODI",
      "LOUX CORNER",
      "LUMBERVILLE",
      "MAPLE BEACH",
      "MECHANICSVILLE",
      "MOUNT PLEASANT",
      "NESHAMINY FALLS",
      "NEWVILLE",
      "OAKFORD",
      "OTTSVILLE",
      "OXFORD VALLEY",
      "PALETOWN",
      "PASSER",
      "PENNS PARK",
      "PINEVILLE",
      "PIPERSVILLE",
      "PLEASANT VALLEY",
      "POINT PLEASANT",
      "REVERE",
      "RUSHLAND",
      "SHELLY",
      "SOLEBURY",
      "SOUTHAMPTON",
      "SPRINGTOWN",
      "STRAWNTOWN",
      "UHLERSTOWN",
      "UNIONVILLE",
      "UPPER BLACK EDDY",
      "WASHINGTON CROSSING",
      "WHITE HORSE",
      "WRIGHTSTOWN",
      "WYCOMBE",
      "ZIONHILL",

      // Lehigh County
      "COOPERSBURG",

      // Montgomery County
      "COLMAR",
      "MARLBORO",
      "MARLBORO TWP",
      "MONTGOMERY TWP",

      // Northampton Couty
      "HELLERTOWN"
  };
}
