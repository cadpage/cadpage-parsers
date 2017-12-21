package net.anei.cadpage.parsers.ND;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NDCassCountyParser extends SmartAddressParser {
 
  public NDCassCountyParser() {
    super(CITY_CODES, "CASS COUNTY", "ND");
    setFieldList("CALL ADDR APT CITY ST PLACE X DATE TIME ID INFO UNIT GPS MAP");
    setupCallList(CALL_LIST);
    removeWords("ESTATES");
    setupSaintNames("FRANCIS");
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets("BROADWAY");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@rrrdc.or,dispatch@cityoffargo.com,dispatch@fargond.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern DATE_TIME_CFS_PTN = Pattern.compile("(?:\\* +)?(?:(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) )?CFS #:? (\\d+) ");
  private static final Pattern CALL_PFX_PTN = Pattern.compile("X - |X-SEND FIRE ", Pattern.CASE_INSENSITIVE);
  private static final Pattern CITY_PTN = Pattern.compile("([^*]+), (?!EXIT|RAMP|RED|THEN)([A-Z]{3,4})\\b *(.*)");
  private static final Pattern UNIT_PTN = Pattern.compile("(?: \\d{3}| \\d{4}-[A-Z]+)+$");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = body.replace('\n', ' ');
    Matcher match = DATE_TIME_CFS_PTN.matcher(body);
    if (!match.find()) return false;
    data.strDate = getOptGroup(match.group(1));
    data.strTime = getOptGroup(match.group(2));
    data.strCallId = match.group(3);
    String sAddr = body.substring(0,match.start()).trim();
    String sInfo = body.substring(match.end()).trim();
    
    int version =  data.strDate.length() > 0 ? 1 : 2;
    
    String prefix = null;
    match = CALL_PFX_PTN.matcher(sAddr);
    if (match.lookingAt()) {
      prefix = match.group();
      sAddr = sAddr.substring(match.end());
    }
    
    StartType st = StartType.START_CALL;
    int flags = FLAG_START_FLD_REQ;
    int pt = sAddr.indexOf(" * ");
    if (pt < 0 && sAddr.endsWith(" *")) pt = sAddr.length()-2;
    if (pt >= 0) {
      data.strCall = sAddr.substring(0, pt).trim();
      sAddr = sAddr.substring(pt+2).trim();
      sAddr = stripFieldStart(sAddr, "*");
      st = StartType.START_ADDR;
      flags = 0;
    }
    
    String extra = null;
    sAddr = stripFieldEnd(sAddr, "*");
    pt = sAddr.indexOf(" * ");
    if (pt >= 0) {
      extra = sAddr.substring(pt+3).trim();
      sAddr = sAddr.substring(0,pt).trim();
      
      String city = CITY_CODES.getProperty(sAddr);
      if (city != null) {
        data.strCity = city;
        sAddr = extra;
        extra = null;
        pt = sAddr.indexOf(" * ");
        if (pt >= 0) {
          extra = sAddr.substring(pt+3).trim();
          sAddr = sAddr.substring(0,pt).trim();
        }
      }
    }
    
    String trail = null;
    match = CITY_PTN.matcher(sAddr);
    if (match.matches()) {
      sAddr = match.group(1).trim();
      data.strCity = convertCodes(match.group(2), CITY_CODES);
      trail = match.group(3);
      flags |= FLAG_ANCHOR_END | FLAG_NO_CITY;
    }
    
    sAddr = sAddr.replace("\\", "&").replace('@',  '&');
    flags |= FLAG_EMPTY_ADDR_OK | FLAG_IMPLIED_INTERSECT;
    if (version >= 2) flags |= FLAG_CROSS_FOLLOWS;
    flags |= FLAG_RECHECK_APT;
    String saveCity = data.strCity;
    parseAddress(st, flags, sAddr, data);
    if (saveCity.length() > 0) data.strCity = saveCity;
    if (prefix != null) data.strCall = prefix + data.strCall;

    if (trail == null) trail = getLeft();
    trail = stripFieldStart(trail, "*");
    parseTrailField(version, trail, data);

    if (extra !=  null) {
      for (String part : extra.split(" \\* ")) {
        part = part.trim();
        parseTrailField(version, part, data);
      }
    }
    
    pt = data.strCity.indexOf('/');
    if (pt >= 0) {
      data.strState = data.strCity.substring(pt+1);
      if (data.strState.equals("ND")) data.strState = "";
      data.strCity = data.strCity.substring(0,pt).trim();
    }
    
    if (version == 1) {
      match = UNIT_PTN.matcher(sInfo);
      if (match.find()) {
        data.strUnit = match.group().trim();
        sInfo = sInfo.substring(0,match.start()).trim();
      }
    } else {
      pt = sInfo.indexOf(" Quad:");
      if (pt >= 0) {
        data.strMap = sInfo.substring(pt+6).trim();
        sInfo = sInfo.substring(0,pt).trim();
      }
    }
    
    pt = sInfo.indexOf(" GPS:");
    if (pt >= 0) {
      setGPSLoc(sInfo.substring(pt+5), data);
      sInfo = sInfo.substring(0,pt).trim();
    }
  
    pt = sInfo.indexOf(" Units:");
    if (pt >= 0) {
      data.strUnit = sInfo.substring(pt+7).trim();
      sInfo =  sInfo.substring(0,pt).trim();
    }
    
    for (String info : sInfo.split("Nature Of Call:")) {
      info = info.trim();
      pt = info.indexOf("E911 Info -");
      if (pt >= 0) {
        info = info.substring(0, pt).trim();
        info = stripFieldEnd(info, "-");
      }
      if (data.strSupp.startsWith(info)) continue;
      if (info.startsWith(data.strSupp)) {
        data.strSupp = info;
      } else {
        data.strSupp = append(data.strSupp, " - ", info);
      }
    }
    return true;
  }

  private static final Pattern X_PLACE_PTN = Pattern.compile("(?:(.*?) )??([A-Z][a-z].*)");

  private void parseTrailField(int version, String field, Data data) {
    Matcher match;
    
    int pt = field.indexOf(" - ");
    if (pt >= 0) field = field.substring(0,pt).trim();
    if (field.length() <= 1) return;
    
    if (data.strCity.length() == 0) {
      String city = CITY_CODES.getProperty(field);
      if (city != null) {
        data.strCity = city;
        return;
      }
    }

    String place = "";
    if (data.strAddress.length() == 0) {
      parseAddress(field, data);
    } else if (version == 2 && data.strCross.length() == 0) {
      if (field.startsWith("No Cross Streets Found")) {
        field = field.substring(22).trim();
        place = stripFieldStart(field, "* ");
      } else if ((match = X_PLACE_PTN.matcher(field)).matches()) {
        data.strCross = getOptGroup(match.group(1)).replace("\\", "/").replace('@',  '/');
        place = match.group(2).trim();
      } else {
        data.strCross = field.replace("\\", "/").replace('@',  '/');;
      }
    } else if (version == 1 && data.strCross.length() == 0 &&
               (field.contains("/") || field.contains("\\") || field.contains("@"))) {
      data.strCross = field.replace("\\", "/").replace('@',  '/');;
    } else {
      place = field;
    }
    data.strPlace = append(place, " - ", data.strPlace);
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "EAST GATEWAY",
      "MAPLE RIVER",
      "TRENT JONES",
      "TURTLE LAKE",
      "AMBER VALLEY",
      "COUNTRYSIDE TRAILER",
      "DAKOTA PARK",
      "NORTHERN PACIFIC",
      "VALLEY VIEW",
      "WHEATLAND PINES"
  };
  
  private static CodeSet CALL_LIST = new CodeSet(
      "<NEW>",
      "01 ABDOMINAL PAIN",
      "01 ABDOMINAL PAIN/PROBLEMS",
      "02 ALLERGIES/ENVENOMATIONS",
      "05 BACK PAIN -  NON TRAUMATIC",
      "05 BACK PAIN - NON TRAUMATIC",
      "06 BREATHING PROBLEMS",
      "07 BURNS/SCALDS",
      "08 INHALATION",
      "09 CARDIAC/RESPIRATORY ARREST",
      "09 CARDIAC/RESPIRATORY ARREST",
      "10 CHEST PAIN",
      "11 CHOKING",
      "12 CONVULSIONS/SEIZURE",
      "13 DIABETIC PROBLEMS",
      "17 FALLS",
      "18 HEADACHE",
      "19 HEART PROBLEMS",
      "20 HEAT - COLD EXPOSURE",
      "21 HEMORRAHAGE - LACERATIONS",
      "21 HEMORRHAGE - LACERATIONS",
      "23 OVERDOSE - POISONING",
      "24 PREGNANCY - CHILDBIRTH",
      "26 SICK PERSON",
      "27 STAB/GUNSHOT/PENETRATING",
      "28 STROKE  - CVA",
      "28 STROKE - CVA",
      "30 TRAUMATIC INJURIES",
      "31 UNCONSCIOUS - FAINTING",
      "32 UNKNOWN PROBLEM - MAN DOWN",
      "33 TRANSFER INTERFACILITY",
      "911 HANG UP",
      "ACCIDENT - INJURY",
      "ACCIDENT - PROPERTY",
      "AIRCRAFT CRASH",
      "ARCING WIRE/TRANSFORMER FIRE",
      "ASSAULT",
      "BKOA",
      "BON FIRE",
      "CARBON MONOXIDE DETECTOR",
      "COMMERCIAL FIRE",
      "COMMERCIAL FIRE ALARM RESET ONLY",
      "DISTURBANCE",
      "DOMESTIC",
      "DUMPSTER FIRE",
      "FIRE ALARM ACTIVATION",
      "FIRE ALARM RESET",
      "GAS/FUEL SPILLS",
      "GAS LEAK",
      "GRASS FIRE",
      "HAZ-MAT INCIDENT",
      "IMPAIRED DRIVER",
      "IMPAIRED PERSON",
      "LIFTING ASSISTANCE",
      "MEDICAL",
      "MEDICAL-SEND FIRE",
      "MEDICAL ASSIST-OFFICER REQUESTED",
      "MENTALLY IMPAIRED",
      "MISC",
      "MISC FIRE",
      "MUTUAL AID FIRE",
      "RESCUE",
      "RESIDENTIAL FIRE",
      "SU",
      "SUICIDAL PERSON",
      "STRUCTURE FIRE",
      "TEST",
      "TS",
      "VEHICLE FIRE",
      "WATER BREAK/WASHED OUT ROAD",
      "WATER BREAK/SEWER/WASH OUT ROAD",
      "WATER RESCUE"
  );
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ABSA", "ABSARAKA/ND",
      "ALIC", "ALICE/ND",
      "AMEN", "AMENIA/ND",
      "ARGU", "ARGUSVILLE/ND",
      "ARTH", "ARTHUR/ND",
      "AVIL", "AVERILL/MN",
      "AYR",  "AYR/ND",
      "BAKE", "BAKER/MN",
      "BARN", "BARNESVILLE/MN",
      "BORU", "BORUP/MN",
      "BRIA", "BRIARWOOD/ND",
      "BUFF", "BUFFALO/ND",
      "CASS", "",
      "CAST", "CASSELTON/ND",
      "CHAF", "CHAFFEE/ND",
      "CHRI", "CHRISTINE/ND",
      "CLAY", "CLAY COUNTY/MN",
      "CLIF", "CLIFFORD/ND",
      "COMS", "COMSTOCK/MN",
      "CROM", "CROMWELL TOWNSHIP/MN",
      "DALE", "DALE/MN",
      "DAVE", "DAVENPORT/ND",
      "DILW", "DILWORTH/MN",
      "DOWN", "DOWNER/MN",
      "DURB", "DURBIN/ND",
      "EGLO", "EGLON TOWNSHIP/MN",
      "ELKT", "ELKTON TOWNSHIP/MN",
      "ELMW", "ELMWOOD TOWNSHIP/MN",
      "EMBD", "EMBDEN/ND",
      "ENDE", "ENDERLIN/ND",
      "ERIE", "ERIE/ND",
      "FELT", "FELTON/MN",
      "FGO",  "FARGO/ND",
      "FING", "FINGAL/ND",
      "FLOW", "FLOWING TOWNSHIP/MN",
      "GALE", "GALESBURG/ND",
      "GARD", "GARDNER/ND",
      "GEOR", "GEORGETOWN/MN",
      "GLYN", "GLYNDON/MN",
      "GOOS", "GOOSE PRAIRIE TOWNSHIP/MN",
      "GRAN", "GRANDIN/ND",
      "HAGE", "HAGEN TOWNSHIP/MN",
      "HAWL", "HAWLEY/MN",
      "HARW", "HARWOOD/ND",
      "HICK", "HICKSON/ND",
      "HIGH", "HIGHLAND GROVE TOWNSHIP/MN",
      "HITT", "HITTERDAL/MN",
      "HOLY", "HOLY CROSS TOWNSHIP/MN",
      "HOPE", "HOPE/ND",
      "HORA", "HORACE",
      "HUMB", "HUMBOLDT TOWNSHIP/MN",
      "HUNT", "HUNTER/ND",
      "KEEN", "KEENE TOWNSHIP/MN",
      "KIND", "KINDRED/ND",
      "KRAG", "KRAGNESS TOWNSHIP/MN",
      "KURT", "KURTZ TOWNSHIP/MN",
      "LEON", "LEONARD/ND",
      "LPRK", "LAKE PARK/MN",
      "MAPL", "MAPLETON/ND",
      "MHD",  "MOORHEAD/MN",
      "MOLA", "MOLAND TOWNSHIP/MN",
      "MORK", "MORKEN TOWNSHIP/MN",
      "NEWR", "NEW ROCKFORD/ND",
      "NORA", "HORACE/ND",
      "NROT", "NORTH RIVER/ND",
      "OAKP", "OAKPORT TOWNSHIP/MN",
      "OXBO", "OXBOW/ND",
      "PAGE", "PAGE/ND",
      "PARK", "PARKE TOWNSHIP/MN",
      "PELI", "PELICAN RAPIDS/MN",
      "PERL", "PERLEY/MN",
      "PRAI", "PRAIRIE ROSE/ND",
      "PROS", "PROSPER/ND",
      "REIL", "REILE'S ACRES/ND",
      "RIVE", "RIVERTON TOWNSHIP/MN",
      "ROGE", "ROGERS/ND",
      "ROLL", "ROLLAG/MN",
      "RRON", "FRONTIER/ND",
      "RUST", "RUSTAD CITY/ND",
      "SABI", "SABIN/MN",
      "SHEL", "SHELDON/ND",
      "SKRE", "SKREE TOWNSHIP/MN",
      "SPRI", "SPRING PRAIRIETOWNSHIP/MN",
      "TANS", "TANSEM TOWNSHIP/MN",
      "TOWE", "TOWER CITY/ND",
      "TWIN", "TWIN VALLEY/MN",
      "ULEN", "ULEN/MN",
      "VIDI", "VIDING TOWNSHIP/MN",
      "WARR", "WARREN/MN",
      "WFGO", "WEST FARGO/ND",
      "WHEA", "WHEATLAND/ND",   // Changed from MN
      "WILD", "WILD RICE/ND",
      "WOLV", "WOLVERTON/MN",
      
      "BECKCO",          "BECKER COUNTY/MN",
      "NORMCO",          "NORMAN COUNTY/MN",
      "RICHCO",          "RICHLAND COUNTY",
      "ROTHSAY",         "ROTHSAY/MN",
      "WALCOT",          "WALCOTT",
      "WALCOTT",         "WALCOTT",
      "WALCOT RICHCO",   "WALCOTT",
      "WALCOTT RICHCO",  "WALCOTT",
      "RICHCO WALCOT",   "WALCOTT",
      "RICHCO WALCOTT",  "WALCOTT",
      "WILKCO",          "WILKIN COUNTY/MN",
      "WILKCO ROTHSAY",  "ROTHSAY/MN",
      "WOLVERTON",       "WOLVERTON/MN"

  });
}
