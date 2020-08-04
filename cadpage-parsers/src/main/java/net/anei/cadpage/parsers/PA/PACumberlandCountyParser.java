package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchArchonixParser;

public class PACumberlandCountyParser extends DispatchArchonixParser {

  public PACumberlandCountyParser() {
    super(CITY_CODES, MA_CITY_CODES, "CUMBERLAND COUNTY", "PA", ARCH_FLG_TWO_PART_CITY);
  }

  @Override
  public String getFilter() {
    return "ccpaeoc@comcast.net,EP911@ccpa.net,dispatch@cgfrems.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern GD_PTN = Pattern.compile("\\bGD\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern ES_PTN = Pattern.compile("\\bES\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern HGWY_PTN = Pattern.compile("\\bHGWY\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern BY_PTN = Pattern.compile("\\bBY\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern MILLER_RD_EXT = Pattern.compile("\\bMILLER RD EXT\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String address) {
    address = GD_PTN.matcher(address).replaceAll("GARDEN");
    address = ES_PTN.matcher(address).replaceAll("ESTATES");
    address = HGWY_PTN.matcher(address).replaceAll("HWY");
    address = BY_PTN.matcher(address).replaceAll("BYPASS");
    address = MILLER_RD_EXT.matcher(address).replaceAll("MILLER RD EXD");
    return super.adjustMapAddress(address);
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (subject.startsWith("[DISPATCH]")) subject = subject.substring(10).trim();
    if (!super.parseMsg(subject,  body, data)) return false;

    // Call can contain a code field

    String code = data.strCall;
    int pt = code.indexOf(' ');
    if (pt >= 0) code = code.substring(0,pt);
    String call = CALL_CODES.getCodeDescription(code);
    if (call != null) {
      data.strCode = code;
      data.strCall = call;
    }

    // The Map (Zone) field contains the CAD zone (which users are not interested in
    // and  a box number
    Parser p = new Parser(data.strMap);
    p.get(' ');
    data.strBox = p.get();
    data.strMap = "";

    // Clean up city field
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    data.strCity = stripFieldStart(data.strCity, "BORO OF ");
    data.strCity = stripFieldStart(data.strCity, "BORO ");
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL").replace("MAP", "BOX");
  }

  private static final CodeTable CALL_CODES = new StandardCodeTable(
      "EROUT",      "EMS ROUTINE/STANDBY",
      "EXFER",      "EMS TRANSFER",
      "F1ST",       "Structure Fire - 1st Alarm",
      "F2ND",       "Structure Fire - 2nd Alarm",
      "F3RD",       "Structure Fire - 3rd Alarm",
      "F4TH",       "Structure Fire - 4th Alarm",
      "FAFA",       "Automatic Fire Alarm",
      "FAFARD",     "Automatic Fire Alarm - Reduced Response",
      "FAIRCR",     "Aircraft Fire",
      "FBARN1",     "Barn Fire - 1st Alarm",
      "FBARN2",     "Barn Fire - 2nd Alarm",
      "FBARN3",     "Barn Fire - 3rd Alarm",
      "FBARN4",     "Barn Fire - 4th Alarm",
      "FCB",        "Controlled Burn",
      "FCODET",     "Carbon Monoxide Detector",
      "FFP",        "FFIRE POLICE",
      "FHMINI",     "Hazardous Materials Incident - Inside Investigation",
      "FHMNS",      "Hazardous Materials Incident - Non-structure",
      "FHMOUI",     "Hazardous Materials Incident - Outside investigation",
      "FHMSTR",     "Hazardous Materials Incident - Structure fire",
      "FINFO",      "FINFORMATION",
      "FLZ",        "Helicopter Landing Zone",
      "FMA",        "Fire Department Medical Assist",
      "FMASTN",     "Mass Transportation Incident",
      "FMISC",      "Miscellaneous Incident",
      "FMISCF",     "FMISC w/ FIRE",
      "FMISCN",     "FMISC NO FIRE",
      "FOTRES",     "Miscellaneous Rescue",
      "FOUI",       "Outside Investigate",
      "FSTR0",      "FWORKING STRUCTR",
      "FSTR1",      "Structure Fire - 1st Alarm",
      "FSTR2",      "Structure Fire - 2nd Alarm",
      "FSTR3",      "Structure Fire - 3rd Alarm",
      "FSTR4",      "Structure Fire - 4th Alarm",
      "FSTR5",      "F5TH ALARM STRUCTURE",
      "FVEHICLE",   "Vehicle Fire",
      "FVEHLG",     "Vehicle Fire- Large",
      "FVEHSM",     "Vehicle Fire- Small",
      "FVSNI",      "Vehicle vs. Structure - No injuries reported",
      "FWARES",     "Water Rescue",
      "FWF",        "Wild Fire",
      "FWFSTR",     "Wild Fire Endangering a Structure",
      "FWFVEH",     "Wild Fire Involving a Vehicle",
      "FXFER1",     "Transfer Assignment - 1st Alarm",
      "FXFER2",     "Transfer Assignment - 3rd Alarm",
      "FXFER3",     "FTRANSFER 3RD ALM",
      "FXFER4",     "Transfer Assignment - 4th Alarm",
      "FXFER5",     "FTRANSFER 5TH ALM",
      "MUTAID",     "Mutual Aid Response",
      "P33",        "PSIGNAL 33",
      "P6308A",     "PUNDERAGE DRINKING ACT",
      "P6308N",     "PUNDERAGE DRINKING N/A",
      "P911HU",     "P911 HANG UP",
      "P96NE",      "PSIGNAL 96 NO EMS",
      "PA64A",      "PACT 64 ACTIVE",
      "PA64NA",     "PACT 64 N/A",
      "PAANI",      "Traffic / Transportation Incident - No injuries reported initially",
      "PABDA",      "PABDUCTION ACTIVE",
      "PABDNA",     "PABDUCTION N/A",
      "PAED",       "PAED",
      "PALMBA",     "PALARM BURGLAR",
      "PALMHP",     "PALARM HOLDUP/PANIC",
      "PALMOS",     "PALARM OOS",
      "PALMSG",     "PALARM SOUNDING",
      "PALMVE",     "PALARM VEHICLE",
      "PANMCP",     "PANIMAL COMPLAINT",
      "PANMFL",     "PANIMAL FND/LOST",
      "PANMNR",     "PANIMAL STRUCK OFF RD",
      "PANMOR",     "PANIMAL STRUCK ON RD",
      "PARSA",      "PARSON ACTIVE",
      "PARSNA",     "PARSON NOT ACTIVE",
      "PASTFE",     "PASSIST FD/EMS",
      "PASTPD",     "PASSIST PD",
      "PBDCHK",     "PBAD CHECK",
      "PBKIA",      "PBREAK-IN ACTIVE",
      "PBKINA",     "PBREAK-IN NOT ACTIVE",
      "PBOLO",      "PBOLO",
      "PBOMB",      "PBOMB THREAT/DEVICE",
      "PCFLIP",     "PCFW/LOITERING ACTIVE",
      "PCFLNA",     "PCFW/LOITERING NOT ACT",
      "PCHCNA",     "PCHILD CUSTODY PROB N/A",
      "PCHCUS",     "PCHILD CUSTODY PROB ACTIVE",
      "PCHENA",     "PCHILD ENDANGERMENT N/A",
      "PCHEND",     "PCHILD ENDANGERMENT ACTIVE",
      "PCHG",       "PCHANGE",
      "PCMA",       "PC/M ACTIVE",
      "PCMNA",      "PC/M N/A /JUST DISC",
      "PCOURT",     "PCOURT",
      "PCRJA",      "PCAR JACKING ACTIVE",
      "PCRJNA",     "PCAR JACKING N/A",
      "PDATA",      "PDATA",
      "PDISTA",     "PDIST/NOISE ACTIVE",
      "PDISTN",     "PDIST/NOISE N/A",
      "PDMV",       "PDIS VEH IN TRAFF",
      "PDMVIN",     "PDIS VEH OFF ROAD/INFO",
      "PDOFA",      "PDRIVE OFF ACTIVE",
      "PDOFNA",     "PDRIVE OFF N/A",
      "PDOMA",      "PDOMESTIC ACTIVE",
      "PDOMAW",     "PDOMESTIC ACTIVE w/ WEAPON",
      "PDOMNA",     "PDOMESTIC N/A",
      "PDUIA",      "PDUI ACTIVE",
      "PDUINA",     "PDUI N/A",
      "PEMER",      "PD EMERGENCY BUTTON",
      "PESCRT",     "PESCORT",
      "PF10A",      "PTHEFT ACTIVE",
      "PF10NA",     "PTHEFT N/A",
      "PF11A",      "PASSAULT/RAPEACTIVE",
      "PF11NA",     "PASSAULT/RAPE N/A",
      "PF12",       "PHOMICIDE J/D J/O",
      "PF12IN",     "PHOMICIDE INFO",
      "PF16",       "PSTOLEN/LOST PLATE",
      "PF1A",       "PSTOLEN VEH ACTIVE",
      "PF1NA",      "PSTOLEN VEH N/A",
      "PF3",        "PVEHICLE ACC INFO",
      "PF4A",       "PHIT N RUN ACTIVE",
      "PF4NA",      "PHIT N RUN N/A",
      "PF5A",       "PWANTED/ESCAPE ACTIVE",
      "PF5INF",     "PWANTED/ESCPE INFO",
      "PF6A",       "PMISS/LOST PERS ACTIVE",
      "PF6NA",      "PMISS/LOST PERS N/A",
      "PF7A",       "PBURGLARY ACTIVE",
      "PF7NA",      "PBURGLARY N/A J/D",
      "PF8A",       "PROBBERY ACTIVE",
      "PF8NA",      "PROBBERY N/A",
      "PF9",        "PLOST/MISS PROPERTY",
      "PFHTA",      "PFIGHT ACTIVE",
      "PFHTAW",     "PFIGHT ACTIVE w/ WEAPON",
      "PFHTNA",     "PFIGHT N/A",
      "PFISBT",     "PFISH/ BOAT COMPLTAINT",
      "PFNDBD",     "PFOUND BODY",
      "PFNDPR",     "PFOUND PROPERTY",
      "PFOOT",      "PFOOT PATROL",
      "PFRDA",      "PFRAUD ACTIVE",
      "PFRDNA",     "PFRAUD N/A",
      "PFU",        "PFOLLOW UP",
      "PFWA",       "PFIREWORKS ACTIVE",
      "PFWNA",      "PFIREWORKS N/A",
      "PGAME",      "PGAME COMM COMPLT",
      "PHARA",      "PHARASSMENT ACTIVE",
      "PHARNA",     "PHARASSMENT N/A",
      "PHIT",       "PHIT NCIC/CLEAN",
      "PHOSTA",     "PHOSTAGE SITUATION",
      "PHSECK",     "PHOUSE CHECK",
      "PHSECX",     "PHOUSE CHECK CANCEL",
      "PIEA",       "PINDECENT EXPOSURE ACTIVE",
      "PIENA",      "PINDECENT EXPOSURE N/A",
      "PINFO",      "PINFORMATION",
      "PKDNA",      "PKIDNAPPING ACTIVE",
      "PKDNNA",     "PKIDNAPPING N/A",
      "PLO",        "PLOCKOUT VEHICLE/HOUSE",
      "PLORO",      "PLOCKOUT RUNNING/OCCUPIED",
      "PMEGAN",     "PMEGANS LAW",
      "PNTXA",      "PINTOXICATED SUBJ ACTIVE",
      "PNTXNA",     "PINTOXICATED SUBJ N/A",
      "POPEN",      "POPEN DOOR/WINDOW",
      "PORDCP",     "PORDINANCE COMPLTAINT",
      "PORDIN",     "PORDINANCE INFO",
      "PPFAA",      "PPFA VIOLATION ACTIVE",
      "PPFANA",     "PPFA VIOLATION N/A",
      "PPKGCP",     "PPARKING COMPLAINT",
      "PPROA",      "PPROSTITUTE ACTIVE",
      "PPRONA",     "PPROSTITUTE N/A",
      "PPRWA",      "PPROWLER ACTIVE",
      "PPRWNA",     "PPROWLER N/A",
      "PPURS",      "PPURSUIT",
      "PRAGA",      "PROAD RAGE ACTIVE",
      "PRAGN",      "PROAD RAGE N/A",
      "PREF",       "PREFERENCE",
      "PREPO",      "PREPO",
      "PRETA",      "PRETAIL THEFT ACTIVE",
      "PRETNA",     "PRETAIL THEFT N/A",
      "PRKDA",      "PRECKLESS DRIVER ACTIVE",
      "PRKDNA",     "PRECKLESS DRIVER N/A",
      "PSCXNG",     "PSCHOOL CROSSING",
      "PSHOT",      "PSHOTS FIRED",
      "PSUICD",     "PSUICIDE NO EMS",
      "PSUSA",      "PSUSP ACTIVITY/PERSON ACTIVE",
      "PSUSNA",     "PSUSP ACTIVITY/PERSON N/A",
      "PTERRA",     "PTERRORIST ACTIVTY",
      "PTFPA",      "PTRAFFIC PROBLEM ACTIVE",
      "PTFPNA",     "PTRAFFIC PROBLEM N/A",
      "PTRAF",      "P TRAFFIC DETAIL",
      "PTRANS",     "PTRANSPORT",
      "PTREA",      "PTRESPASSING ACTIVE",
      "PTRENA",     "PTRESPASSING N/A",
      "PTS",        "PTRAFFIC STOP",
      "PUTY",       "PUTILITY",
      "PUUSEA",     "PUNAUTHORIZED USE ACTIVE",
      "PWFCK",      "PWELFARE CHECK",
      "TERROR",     "TERRORISM INFO",
      "TMI",        "TMI",
      "TMIALT",     "TMI ALERT",
      "TMIGEM",     "TMI GENERAL EMERGENCY",
      "TMIPUB",     "TMI PUBLIC INTEREST",
      "TMISAE",     "TMI SITE AREA EMERGENCY",
      "TMIUNE",     "TMI UNUSUAL EVENT",
      "WF26AD",     "WEATHER ADVISEMENT",
      "WF26SU",     "WEATHER SUPPLEMENT",
      "WF26WA",     "WEATHER WATCH",
      "WF26WN",     "WEATHER WARNING",
      "XFER",       "Transfer"

  );

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AC AC", "ABBOTTSTOWN",
      "CA CU", "CARLISLE",
      "CB CU", "CARLISLE BARRACKS",
      "CH CU", "CAMP HILL",
      "CK CU", "COOKE TWP",
      "DK CU", "DICKINSON TWP",
      "EP CU", "EAST PENNSBORO TWP",
      "HM CU", "HAMPDEN TWP",
      "HW CU", "HOPEWELL TWP",
      "LA CU", "LOWER ALLEN TWP",
      "LB CU", "LEMOYNE",
      "LF CU", "LOWER FRANKFORD TWP",
      "LM CU", "LOWER MIFFLIN TWP",
      "MB CU", "MECHANICSBURG",
      "MH CU", "MT HOLLY SPRINGS",
      "MN CU", "MONROE TWP",
      "MX CU", "MIDDLESEX TWP",
      "NB CU", "NEWBURG",
      "NC CU", "NEW CUMBERLAND",
      "NM CU", "NORTH MIDDLETON TWP",
      "NN CU", "NORTH NEWTON TWP",
      "NV CU", "NAVAL SUPPORT ACTIVITY",
      "NW CU", "NEWVILLE",
      "PN CU", "PENN TWP",
      "SB CU", "SHIPPENSBURG",
      "SF CU", "SHIPPENSBURG", // in Franklin County
      "SH CU", "SOUTHAMPTON TWP",
      "SM CU", "SOUTH MIDDLETON TWP",
      "SN CU", "SOUTH NEWTON TWP",
      "SS CU", "SILVER SPRING TWP",
      "ST CU", "SHIPPENSBURG TWP",
      "SR CU", "SHIREMANSTOWN",
      "UA CU", "UPPER ALLEN TWP",
      "UF CU", "UPPER FRANKFORD TWP",
      "UM CU", "UPPER MIFFLIN TWP",
      "WB CU", "WORMLEYSBURG",
      "WP CU", "WEST PENNSBORO TWP",

      "AR FC", "AYR TWP",
      "BC FC", "BRUSH CREEK TWP",
      "BL FC", "BELFAST TWP",
      "BT FC", "BETHEL TWP",
      "DB FC", "DUBLIN TWP",
      "LC FC", "LICKING CREEK TWP",
      "MC FC", "MCCONNELLSBURG",
      "TD FC", "TODD TWP",
      "TH FC", "THOMPSON TWP",
      "TY FC", "TAYLOR TWP",
      "UN FC", "UNION TWP",
      "VH FC", "VALLEY-HI",
      "WL FC", "WELLS TWP",

      "DC DC", "DAUPHIN COUNTY",
      "FCR",   "FRANKLIN COUNTY",
      "FC FR", "FRANKLIN COUNTY",
      "PC PC", "PERRY COUNTY",
      "YC YC", "YORK COUNTY"
  });

  private static final Properties MA_CITY_CODES = buildCodeTable(new String[]{
      "FVT",    "FAIRVIEW TWP",
      "FVW TWP","FAIRVIEW TWP",
      "GREEN",  "GREENE TWP",
      "HBG",    "HARRISBURG",
      "S HAMP", "SOUTHAMPTON TWP",
      "SH TWP", "SOUTHAMPTON TWP",
      "SHT",    "SOUTHAMPTON TWP",
      "SHAMP",  "SOUTHAMPTON TWP",
      "SHP",    "SHIPPENSBURG",
      "SUSQ TWP","SUSQUEHANNA TWP"

  });

}
