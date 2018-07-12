package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INMarionCountyParser extends MsgParser {
  
  public INMarionCountyParser() {
    super("MARION COUNTY", "IN");
    setFieldList("ADDR CITY APT MAP CALL UNIT INFO ID INFO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "CAD@pager.mecagov.org,CAD@page.indy.gov,777,888,93001,72436";
  }
  
  private static final Pattern EX_PTN = Pattern.compile("\\bEX\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = EX_PTN.matcher(addr).replaceAll("EXPW");
    return super.adjustMapAddress(addr);
  }
  
  private static final Pattern MARKER = Pattern.compile("COI PUBLIC SAFETY CAD:|PUBLIC SAFETY CAD:? CAD:|CAD:|direct:");
  private static final String APT_PTN = "(?:#(?:APT|RM|ROOM|SUIT|UNIT)? *((?![NS]\\d{5}\\b)[^ ]+)? )";
  private static final String MAP_PTN = "(?:([NS]\\d{5} [EW]\\d{5}|[NS] +[EW]|SHENDR EHENDR) )";
  private static final Pattern MASTER1 = Pattern.compile("([^,]+), *([A-Z]{3}) " + APT_PTN + "?" + MAP_PTN + "?(.*)");
  private static final Pattern MASTER2 = Pattern.compile("([^,]+?) " + APT_PTN + "?" + MAP_PTN + "(.*)");
  private static final Pattern CALL_ID_PTN = Pattern.compile("\\b(I\\d{4,5})\\.?$");
  private static final Pattern UNIT_PTN =
      Pattern.compile("\\b((?:(?:[A-Z]+[0-9]+[A-Z]?|BC\\d+[A-Z]?|\\d+CIV|\\d+FF|\\d+GRP|[A-Z]+GRP|\\d+TN|\\d+WAY|[A-Z]+FYI|[A-Z]{2}FD|[A-Z]{3}ADM|[A-Z]{2}TF|ALS|AMO|BKCREG|BKCRMD|COMM|COMVAN|DHS|FYI[A-Z]+|CMND|DECSCH|HEMAPG|HINDOT|HMEDIA|IFDEDO|IFDHAZ|IFSPOP|INDOT|IPAGE|IUPAGE|MAJOR|MANPOW|MAYDAY|MEDIA|NONEMG|PVTBLS|PVTMED|SFDOPS|STI|WPAGE|WRKFIR|XLFCHF|XLFINV|XLMCOR|WAYVIC|WRTFPK|WRTFWY|(?:IFD|PIK)[A-Z]{1,3}|99\\d|(?:1ST|2ND|3RD)[A-Z]{3})\\b *)+)[\\., ]*(.*)");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end()).trim();
    
    if (subject.endsWith(" MAJOR PAGE FYI")) {
      data.strSupp = body;
      body = subject.substring(0,subject.length()-15).trim();
    }
    
    else {
      pt = body.lastIndexOf('@');
      if (pt >= 0) {
        data.strSupp = body.substring(pt+1).trim();
        body = body.substring(0,pt).trim();
      }
    }
    
    match = MASTER1.matcher(body);
    String sAddr, sApt, sMap, sExtra;
    if (match.matches()) {
      sAddr = match.group(1);
      data.strCity = convertCodes(match.group(2), CITY_CODES);
      sApt = match.group(3);
      sMap = match.group(4);
      sExtra = match.group(5);
    }
    
    else if ((match = MASTER2.matcher(body)).matches()) {
      sAddr = match.group(1);
      sApt = match.group(2);
      sMap = match.group(3);
      sExtra = match.group(4);
    }
    
    else return false;
    
    parseAddress(sAddr.trim(), data);
    data.strApt = append(data.strApt, "-", getOptGroup(sApt));
    data.strMap = getOptGroup(sMap).replaceAll("  +", " ");
    sExtra = stripFieldStart(sExtra.trim(), "*");
    
    match = CALL_ID_PTN.matcher(sExtra);
    if (match.find()) {
      data.strCallId = match.group(1);
      sExtra = sExtra.substring(0,match.start()).trim();
    }

    String call = CODE_SET.getCode(sExtra);
    if (call != null) {
      data.strCall = call;
      sExtra = sExtra.substring(call.length()).trim();
      match = UNIT_PTN.matcher(sExtra);
      if (match.matches()) {
        data.strUnit = match.group(1).trim();
        sExtra = match.group(2);
      }
    } else {
      match = UNIT_PTN.matcher(sExtra);
      if (match.find()) {
        data.strCall = sExtra.substring(0,match.start()).trim();
        data.strUnit = match.group(1).trim();
        sExtra = match.group(2);
      } else {
        Parser p = new Parser(sExtra);
        data.strCall = p.get(' ');
        sExtra = p.get();
      }
    }
    match = UNIT_PTN.matcher(sExtra);
    if (match.matches()) {
      data.strUnit = match.group(1).trim();
      sExtra = match.group(2);
    }
    data.strSupp = append(sExtra, " / ", data.strSupp);
    return true;
  }
  
  @Override
  public CodeSet getCallList() {
    return CODE_SET;
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{

      "3300 A CHECKPOINT",                    "+39.714466,-86.298459",
      "3218 A CONCOURSE",                     "+39.714466,-86.298459",
      "3301 A CONCOURSE",                     "+39.714466,-86.298459",
      "3302 A CONCOURSE",                     "+39.714466,-86.298459",
      "3303 A CONCOURSE",                     "+39.714466,-86.298459",
      "4302 A CONCOURSE",                     "+39.714466,-86.298459",
      "4304 A CONCOURSE",                     "+39.714466,-86.298459",
      "5302 A CONCOURSE",                     "+39.714466,-86.298459",
      "5305 A CONCOURSE",                     "+39.714466,-86.298459",
      "6303 A CONCOURSE",                     "+39.714466,-86.298459",
      "6311 A CONCOURSE",                     "+39.714466,-86.298459",
      "6312 A CONCOURSE",                     "+39.714466,-86.298459",
      "7300 A CONCOURSE",                     "+39.714466,-86.298459",
      "7301 A CONCOURSE",                     "+39.714466,-86.298459",
      "7303 A CONCOURSE",                     "+39.714466,-86.298459",
      "8307 A CONCOURSE",                     "+39.714466,-86.298459",
      "8308 A CONCOURSE",                     "+39.714466,-86.298459",
      "8309 A CONCOURSE",                     "+39.714466,-86.298459",
      "9217 A CONCOURSE",                     "+39.714466,-86.298459",
      "9225 A CONCOURSE",                     "+39.714466,-86.298459",
      "9237 A CONCOURSE",                     "+39.714466,-86.298459",
      "9301 A CONCOURSE",                     "+39.714466,-86.298459",
      "9303 A CONCOURSE",                     "+39.714466,-86.298459",
      "3300 B CHECKPOINT",                    "+39.714466,-86.298459",
      "3225 B CONCOURSE",                     "+39.714466,-86.298459",
      "3300 B CONCOURSE",                     "+39.714466,-86.298459",
      "3301 B CONCOURSE",                     "+39.714466,-86.298459",
      "4208 B CONCOURSE",                     "+39.714466,-86.298459",
      "4209 B CONCOURSE",                     "+39.714466,-86.298459",
      "4301 B CONCOURSE",                     "+39.714466,-86.298459",
      "4306 B CONCOURSE",                     "+39.714466,-86.298459",
      "5227 B CONCOURSE",                     "+39.714466,-86.298459",
      "5304 B CONCOURSE",                     "+39.714466,-86.298459",
      "5307 B CONCOURSE",                     "+39.714466,-86.298459",
      "5308 B CONCOURSE",                     "+39.714466,-86.298459",
      "5309 B CONCOURSE",                     "+39.714466,-86.298459",
      "5317 B CONCOURSE",                     "+39.714466,-86.298459",
      "6213 B CONCOURSE",                     "+39.714466,-86.298459",
      "6302 B CONCOURSE",                     "+39.714466,-86.298459",
      "6303 B CONCOURSE",                     "+39.714466,-86.298459",
      "6308 B CONCOURSE",                     "+39.714466,-86.298459",
      "6309 B CONCOURSE",                     "+39.714466,-86.298459",
      "7301 B CONCOURSE",                     "+39.714466,-86.298459",
      "7303 B CONCOURSE",                     "+39.714466,-86.298459",
      "7304 B CONCOURSE",                     "+39.714466,-86.298459",
      "8202 B CONCOURSE",                     "+39.714466,-86.298459",
      "8301 B CONCOURSE",                     "+39.714466,-86.298459",
      "8304 B CONCOURSE",                     "+39.714466,-86.298459",
      "8305 B CONCOURSE",                     "+39.714466,-86.298459",
      "9218 B CONCOURSE",                     "+39.714466,-86.298459",
      "9301 B CONCOURSE",                     "+39.714466,-86.298459",
      "9303 B CONCOURSE",                     "+39.714466,-86.298459",
      
      "3192 DERBY WY",                        "+39.809443,-86.399023",
      "3204 DERBY WY",                        "+39.809784,-86.399047",
      "3212 DERBY WY",                        "+39.810283,-86.398983",
      "3224 DERBY WY",                        "+39.810287,-86.398672",
      "3260 DERBY WY",                        "+39.810275,-86.398066",
      "3276 DERBY WY",                        "+39.810303,-86.397755",
      "3284 DERBY WY",                        "+39.809652,-86.397678",
      "3298 DERBY WY",                        "+39.809445,-86.397672",

      "7130 LACABREAH DR",                    "+39.809396,-86.398337",
      "7231 LACABREAH DR",                    "+39.808656,-86.396704",
      "7245 LACABREAH DR",                    "+39.808596,-86.396283",
      "7246 LACABREAH DR",                    "+39.809166,-86.396286",
      "7260 LACABREAH DR",                    "+39.809104,-86.395884",
      "7286 LACABREAH DR",                    "+39.808954,-86.395305",
      "7304 LACABREAH DR",                    "+39.808674,-86.395058",
      "7319 LACABREAH DR",                    "+39.808386,-86.395734",
      "7337 LACABREAH DR",                    "+39.808007,-86.395734",
      "7356 LACABREAH DR",                    "+39.807768,-86.394994",
      "7370 LACABREAH DR",                    "+39.807385,-86.395107",
      "7399 LACABREAH DR",                    "+39.807603,-86.395949",
      "7415 LACABREAH DR",                    "+39.807426,-86.396400",
      
      "3375 TERMINAL WAY",                    "+39.714466,-86.298459",
      "4105 TERMINAL WAY",                    "+39.714466,-86.298459",
      "4306 TERMINAL WAY",                    "+39.714466,-86.298459",
      "9102 TERMINAL WAY",                    "+39.714466,-86.298459",
      "9116 TERMINAL WAY",                    "+39.714466,-86.298459",
      "9214 TERMINAL WAY",                    "+39.714466,-86.298459",
      "9301 TERMINAL WAY",                    "+39.714466,-86.298459",
      "9304 TERMINAL WAY",                    "+39.714466,-86.298459",
      "9312 TERMINAL WAY",                    "+39.714466,-86.298459",
      "9323 TERMINAL WAY",                    "+39.714466,-86.298459",
      "9324 TERMINAL WAY",                    "+39.714466,-86.298459",
      "10106 TERMINAL WAY",                   "+39.714466,-86.298459",
      "10112 TERMINAL WAY",                   "+39.714466,-86.298459",
      "10114 TERMINAL WAY",                   "+39.714466,-86.298459",
      "10120 TERMINAL WAY",                   "+39.714466,-86.298459",
      "10237 TERMINAL WAY",                   "+39.714466,-86.298459",
      "10301 TERMINAL WAY",                   "+39.714466,-86.298459",
      "10302 TERMINAL WAY",                   "+39.714466,-86.298459",
      "10307 TERMINAL WAY",                   "+39.714466,-86.298459",
      "10310 TERMINAL WAY",                   "+39.714466,-86.298459",
      "10314 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11101 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11102 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11105 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11113 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11205 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11210 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11301 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11302 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11304 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11312 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11316 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11338 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11340 TERMINAL WAY",                   "+39.714466,-86.298459",
      "11376 TERMINAL WAY",                   "+39.714466,-86.298459",
      "12107 TERMINAL WAY",                   "+39.714466,-86.298459",
      "13110 TERMINAL WAY",                   "+39.714466,-86.298459",
      "13111 TERMINAL WAY",                   "+39.714466,-86.298459",
      "13208 TERMINAL WAY",                   "+39.714466,-86.298459",
      "13211 TERMINAL WAY",                   "+39.714466,-86.298459",
      "13312 TERMINAL WAY",                   "+39.714466,-86.298459",
      "14201 TERMINAL WAY",                   "+39.714466,-86.298459",
      "14300 TERMINAL WAY",                   "+39.714466,-86.298459",
      "14309 TERMINAL WAY",                   "+39.714466,-86.298459",
      "14313 TERMINAL WAY",                   "+39.714466,-86.298459",
      "15300 TERMINAL WAY",                   "+39.714466,-86.298459",
      "15301 TERMINAL WAY",                   "+39.714466,-86.298459",
      "16300 TERMINAL WAY",                   "+39.714466,-86.298459",
      "16312 TERMINAL WAY",                   "+39.714466,-86.298459",
      "16315 TERMINAL WAY",                   "+39.714466,-86.298459",
      "17324 TERMINAL WAY",                   "+39.714466,-86.298459",
      
      "59 I70 EB",                            "+39.613621,-86.479988",
      "59 I70 WB",                            "+39.613621,-86.479988",
      "60 I70 EB",                            "+39.616998,-86.469243",
      "60 I70 WB",                            "+39.616998,-86.469243",
      "61 I70 EB",                            "+39.624576,-86.455704",
      "61 I70 WB",                            "+39.624576,-86.455704",
      "62 I70 EB",                            "+39.634080,-86.440289",
      "62 I70 WB",                            "+39.634080,-86.440289",
      "63 I70 EB",                            "+39.642463,-86.422976",
      "63 I70 WB",                            "+39.642463,-86.422976",
      "64 I70 EB",                            "+39.648398,-86.407061",
      "64 I70 WB",                            "+39.648398,-86.407061",
      "65 I70 EB",                            "+39.656745,-86.392618",
      "65 I70 WB",                            "+39.656745,-86.392618",
      "66 I70 EB",                            "+39.668423,-86.370042",
      "66 I70 WB",                            "+39.668423,-86.370042",
      "67 I70 EB",                            "+39.674615,-86.354182",
      "67 I70 WB",                            "+39.674615,-86.354182",
      "68 I70 EB",                            "+39.680859,-86.338814",
      "68 I70 WB",                            "+39.680859,-86.338814",
      "69 I70 EB",                            "+39.688153,-86.322482",
      "69 I70 WB",                            "+39.688153,-86.322482",
      "70 I70 WB",                            "+39.693588,-86.307938",
      
      "I70 075 RAMP A",                       "+39.729952,-86.242714",
      "I70 077 RAMP A",                       "+39.751322,-86.227014",
      "I70 077 RAMP B",                       "+39.751810,-86.223286",

      "61 I74 EB",                            "+39.879654,-86.472397",
      "61 I74 WB",                            "+39.879654,-86.472397",
      "62 I74 EB",                            "+39.880013,-86.454378",
      "62 I74 WB",                            "+39.880013,-86.454378",
      "63 I74 EB",                            "+39.879290,-86.439920",
      "63 I74 WB",                            "+39.879290,-86.439920",
      "64 I74 EB",                            "+39.871357,-86.423678",
      "64 I74 WB",                            "+39.871357,-86.423678",
      "65 I74 EB",                            "+39.863561,-86.410338",
      "65 I74 WB",                            "+39.863561,-86.410338",
      "66 I74 EB",                            "+39.860220,-86.394172",
      "66 I74 WB",                            "+39.860220,-86.394172",
      "67 I74 EB",                            "+39.851078,-86.374008",
      "67 I74 WB",                            "+39.851078,-86.374008",
      "68 I74 EB",                            "+39.841117,-86.356326",
      "68 I74 WB",                            "+39.841117,-86.356326",
      "69 I74 EB",                            "+39.835114,-86.343693",
      "69 I74 WB",                            "+39.835114,-86.343693",
      "70 I74 EB",                            "+39.830403,-86.326885",
      "70 I74 WB",                            "+39.830403,-86.326885",
      
      "I74 EB & RONALD REAGAN OFFRAMP",       "+39.840937,-86.355676",
      "I74 WB & RONALD REAGAN OFFRAMP",       "+39.840937,-86.355676",
      "I74 EB & RONALD REAGAN ONRAMP",        "+39.840937,-86.355676",
      "I74 WB & RONALD REAGAN ONRAMP",        "+39.840937,-86.355676",
      "I74 EB & SR 267 ONRAMP EB",            "+39.859832,-86.392501",
      "I74 WB & SR 267 ONRAMP WB",            "+39.859832,-86.392501",
      
      "I465 011 RAMP A",                      "+39.728231,-86.263175",
      "I465 012 RAMP A",                      "+39.746920,-86.263479",
      "I465 013 RAMP A",                      "+39.763286,-86.265185",
      "I465 014 RAMP A",                      "+39.777480,-86.269702",
      "I465 017 RAMP A",                      "+39.821698,-86.274513",
      "I465 017 RAMP D",                      "+39.822587,-86.276850",
      
      "RONALD REAGAN OFFRAM & I74",           "+39.840937,-86.355676",
      "RONALD REAGAN ONRAMP/I74 EB",          "+39.840937,-86.355676",
      "RONALD REAGAN ONRAMP/I74 WB",          "+39.840937,-86.355676",
      "RONALD REAGAN OFFRAM/I74",             "+39.840937,-86.355676",
      "RONALD REAGAN OFFRAM/I74 EB",          "+39.840937,-86.355676",
      "RONALD REAGAN OFFRAM/I74 WB",          "+39.840937,-86.355676",
      
      "SR 267 ONRAMP EB & I74",               "+39.859832,-86.392501",
      "SR 267 ONRAMP WB & I74",               "+39.859832,-86.392501",
      "SR 267 ONRAMP EB & I74 EB",            "+39.859832,-86.392501",
      "SR 267 ONRAMP WB & I74 WB",            "+39.859832,-86.392501",

      "11 E NORTHFIELD DR",                   "+39.857278,-86.391256",
      "40 E NORTHFIELD DR",                   "+39.857289,-86.391114",
      "50 E NORTHFIELD DR",                   "+39.857289,-86.391114",
      "80 E NORTHFIELD DR",                   "+39.857289,-86.391114",
      "124 E NORTHFIELD DR",                  "+39.857308,-86.390631",
      "250 E NORTHFIELD DR",                  "+39.857062,-86.389243",
      "301 E NORTHFIELD DR",                  "+39.856775,-86.388017"
      
  });

  private static final CodeSet CODE_SET = new CodeSet(
      "ABDOMIN/BACK PN",
      "ALLERGIC REACTIO",
      "ANIMAL BITE",
      "APARTMENT",
      "APARTMNT/WORKI",
      "APT ALARM",
      "APT/ENTRAPMENT",
      "ASLT/TRM/UNSECUR",
      "ASSAULT/TRAUMA",
      "ASSAULT/TRAUMA-C",
      "ASSIST PERSON",
      "BLEED/NONTRAU-C",
      "BLDG/HR/ALARM",
      "BLDG/HR/WORKING",
      "BLEEDING/NONTRAU",
      "BUILD ALARM",
      "BUILDING",
      "BUILDING/WORKI",
      "BURGLARY/IN-PROG",
      "BURNED PERSON",
      "CARD/ARREST/WRKG",
      "CARDIAC ARREST",
      "CHEMICAL SPILL",
      "CHEST PAIN/HRT",
      "CIV/FATALITY",
      "CIV/SLIGHT/INJ",
      "CO DETECTOR",
      "DBL RES/ENTR/WRK",
      "DEPT VEH ACCIDNT",
      "DET",
      "DIABETIC",
      "DIFF BREATHIN",
      "DIFF BREATHING",
      "DOUBL RES/ENTRAP",
      "DOUBLE RESI/WRK",
      "DOUBLE RESIDENCE",
      "DROWN/RESCUE",
      "ELEVATOR",
      "EMER TRANSFER",
      "EMS/UNKNOWN",
      "ENVIRONMENTAL",
      "EXPLOSION",
      "FF/SLIGHT/INJ",
      "FF/TRANSPORTED",
      "FIELD",
      "FLULIKE SYMPTOMS",
      "GARAGE",
      "GARAGE/WORKI",
      "GAS MAIN RUPTU",
      "GAS ODOR/BUILD",
      "GAS ODOR/OUTSIDE",
      "GRASS/LEAVES",
      "GUNSHOT",
      "GUNSHOT/UNSECURE",
      "GYNECOLOGY",
      "HAZ-MAT WORK",
      "HEADACHE",
      "HOSP/NUR HM/WORK",
      "HOSP/NUR HR/ALRM",
      "HOSP/NURSING HM",
      "H/R RPT",
      "INC/CALL/BUILD",
      "INJ/CIV/TRANSP",
      "INJ/FF/TRANSP",
      "INJURED/EXTRICAT",
      "INJURED PERSON",
      "INJURED PERSON-C",
      "INVERTED VEHICLE",
      "INVESTIGATE",
      "LARGE SPILL",
      "LOCK IN/OUT BLDG",
      "MASS CASUALTY 1",
      "MEDICAL ALARM",
      "MEET THE POLICE",
      "MENT/ILL/UNSECUR",
      "MENTAL ILLNESS",
      "MINOR PI",
      "MOD STRUC/COLLPS",
      "MOTORCYCLE PI",
      "NEURO/HEAD INJ",
      "OB/CHILDBIRTH",
      "OVERDOSE",
      "OVERDOSE/UNSECUR",
      "PEDESTRN STRUCK",
      "PERS LOCKED/VEH",
      "PERSON CHOKING",
      "PI TACTICAL",
      "PI/TACTICAL",
      "PI TACTICAL/WORK",
      "PI W/ENTRAPMENT",
      "PI W/ ENTRAPMENT",
      "PI W/EXTRAPMENT",
      "PI WORK/ENTRAP",
      "POWER LINES DOWN",
      "PROJ LF/SVR SRCH",
      "REACTION",
      "REQUEST ASSIST",
      "RESID ALARM",
      "RESIDENCE",
      "RES ENTRAP/WRK",
      "RES ENTRAPMNT",
      "RESIDENCE/WORKIN",
      "RESD FOR SWAT CALLOUT",
      "RESD REF SWAT CALLOUT",
      "RESD REQ SWAT CALLOUT",
      "ROPE RESCUE",
      "S E PI W/ENTRAPMENT",
      "SCHOOL ALARM",
      "SCHOOL/WORKING",
      "SEIZURE",
      "SEMI/RV/MOTHM",
      "SICK AND DIZZY",
      "SICK PERSON",
      "SICK PERSON-C",
      "SMALL SPILL",
      "SMALL SPILL/WRK",
      "SMELL OF SMOKE",
      "SPECIAL DETAIL",
      "SPILL/FIRE",
      "STABBING",
      "STABBING/UNSECUR",
      "STANDBY",
      "STR/COLLAPSE/WRK",
      "STROKE/CVA",
      "STRUCT/COLLAPSE",
      "TEST INCIDENT",
      "TOOTH ACHE",
      "TRAILOR/MOBIL HM",
      "TRASH",
      "UNCON PERSON",
      "UNCONSCIOUS PERS",
      "UNKNOWN/FIRE",
      "UNKNOWN SUBST",
      "UNRESPONSIVE",
      "URBAN SEARCH",
      "UTILITY POLE",
      "VEHICL/ACCIDENT",
      "VEHICLE FIRE",
      "WATER RECOVERY",
      "WATER RESCUE INV",
      
      // WTF???
      "F",
      "N"
  );
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{

      "AIR", "Indianapolis",
      "AMO", "Amo",
      "AVN", "Avon",
      "BBG", "Brownsburg",
      "BGR", "Beech Grove",
      "BON", "Lebanon",
      "CAM", "Camby",
      "CAR", "Cartersburg",
      "CLR", "Clermont",
      "CLY", "Clayton",
      "CTV", "Coatsville",
      "DAN", "Danville",
      "HOM", "Homecroft",
      "IND", "Indianapolis",
      "LAW", "Lawrence",
      "LIZ", "Lizton",
      "MOR", "Mooresville",
      "MOV", "Monrovia",
      "NSF", "North Salem",
      "PIT", "Pittsboro",
      "PLF", "Plainfield",
      "STI", "Stilesville",

      "DEC", "Decatur TWP",
      "FRA", "Franklin Township",
      "LWR", "Lawrence TWP",
      "PER", "Perry TWP",
      "PIK", "Pike TWP",
      "SPD", "Speedway",
      "WAR", "Warren TWP",
      "WAS", "Washington TWP",
      "WAY", "Wayne TWP",
      
      "HAM", "Hamilton County"
  });
}
