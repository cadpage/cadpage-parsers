package net.anei.cadpage.parsers.NJ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NJSussexCountyAParser extends SmartAddressParser {
  
  public NJSussexCountyAParser() {
    this("SUSSEX COUNTY", "NJ");
  }
  
  public NJSussexCountyAParser(String defCity, String defState) {
    super(defCity, defState);
    setFieldList("ID CODE CALL BOX PLACE ADDR X APT CITY ST INFO UNIT");
  }
  
  @Override
  public String getAliasCode() {
    return "NJSussexCountyA";
  }
  
  @Override
  public String getFilter() {
    return "@nwbcd.org,@middle,@englewoodpd.org,@enforsys.com,@atpdtext.org,alerts@atpd.org,wcpdicm@enforsys.com,@Enfwebmail.onmicrosoft.com,ems911page@twpoceannj.gov,donotreply@Enfwebmail.onmicrosoft.com,westcaldwellpd@enfwebmail.onmicrosoft.com";
  }
  
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("[A-Z]{1,5}-?[A-Z]?\\d{4}-?\\d{5,6}(?: .*)?");
  private static final Pattern TRAIL_MARK_PTN = Pattern.compile("[-.\\s]*\n\\*{1,3}This\\semail");
  private static final Pattern UNIT_JUNK_PTN = Pattern.compile(" - (?:CAR|RESCUE|ENGINE|TOWER|TRUCK|TENDER) \\d+");
  private static final Pattern MASTER_PTN = 
    Pattern.compile("([-/.,;&A-Za-z0-9 ]+) @ (?:BOX (\\S+) - )?(?:([^,]*) - )?([^,]+?) *(?:, ([^-\\.]*)(?:\\. -| -|\\.|$)| |(?<! )-(?= )|$)(?: (.*?)[-\\.]*)?", Pattern.DOTALL);
  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("(.*?)(?: ([A-Z]{2}))?(?: \\d{5})?");
  private static final Pattern END_STAR_PTN = Pattern.compile("([A-Z0-9])\\*");
  private static final Pattern LEAD_INFO_JUNK_PTN = Pattern.compile("^[-\\*\\. ]+");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    subject = stripFieldStart(subject, "(");
    subject = stripFieldEnd(subject, ")");
    if (!subject.equals("Fire Department")) {
      if (! SUBJECT_PTN.matcher(subject).matches()) return false;
      data.strCallId = subject;
    }
    
    Matcher match = TRAIL_MARK_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();

    String trailInfo = "";
    int pt = body.indexOf("\nActive Units:");
    if (pt >= 0) {
      String unit = body.substring(pt+14).trim();
      body = body.substring(0, pt).trim();
      
      unit = UNIT_JUNK_PTN.matcher(unit).replaceAll("");
      pt = unit.indexOf(" - ");
      if (pt >= 0) {
        trailInfo = unit.substring(pt+3).trim();
        unit = unit.substring(0, pt).trim();
      } else {
        unit = stripFieldEnd(unit, "-");
      }
      data.strUnit = unit.replace(' ', '_');
    }
    
    
    body = body.replace('\n', ' ');
    match = MASTER_PTN.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim().toUpperCase();
    String call = CALL_CODES.getProperty(data.strCall);
    if (call != null) {
      data.strCode = data.strCall;
      data.strCall = call;
    }
    
    data.strBox = getOptGroup(match.group(2));
    data.strPlace = getOptGroup(match.group(3));
    parseAddress(StartType.START_ADDR, FLAG_NO_CITY | FLAG_ANCHOR_END | FLAG_RECHECK_APT, match.group(4).trim(), data);
    String city = getOptGroup(match.group(5));
    String sInfo = getOptGroup(match.group(6));
    
    pt = city.lastIndexOf(',');
    if (pt >= 0) {
      data.strApt = append(data.strApt, ", ", city.substring(0,pt).trim());
      city = city.substring(pt+1).trim();
    }
    
    match = CITY_ST_ZIP_PTN.matcher(city);
    if (match.matches()) {
      city = match.group(1).trim();
      data.strState = getOptGroup(match.group(2));
    }
    
    if (city.equals("CMCH")) city = "CAPE MAY COURT HOUSE";
    data.strCity = city;
    
    if (data.strCity.equals("OUT OF TOWN")) {
      data.strCity = data.defCity = data.defState = "";
    }
    if (data.strAddress.equals("OUT OF TOWN")) {
      sInfo = stripFieldStart(sInfo, "*");
      sInfo = END_STAR_PTN.matcher(sInfo).replaceFirst("$1 *");
      Result res = parseAddress(StartType.START_OTHER, FLAG_IGNORE_AT, sInfo);
      if (res.isValid()) {
        String defCity = data.strCity;
        data.strCity = "";
        data.strAddress = "";
        res.getData(data);
        data.strCall = append(data.strCall, " - ", res.getStart());
        data.strSupp = res.getLeft();
        if (data.strCity.length() == 0) {
          if (data.strSupp.startsWith("IN ")) {
            Parser p = new Parser(data.strSupp.substring(3).trim());
            data.strCity = p.get(' ');
            if (data.strCity.length() > 0) {
              data.strSupp = p.get();
            }
          }
          else if (data.strSupp.startsWith("(")) {
            pt = data.strSupp.indexOf(')');
            if (pt >= 0) {
              data.strCity = data.strSupp.substring(1,pt).trim();
              data.strSupp = data.strSupp.substring(pt+1).trim();
            }
          }
          if (data.strCity.length() == 0) data.strCity = defCity;
          if (data.strCity.length() == 0) data.defCity = data.defState = "";
        }
      } else {
        data.strSupp = sInfo;
      }
    } else {
      data.strSupp = sInfo;
    }
    data.strCity = stripFieldEnd(data.strCity, " BOROUGH");
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    data.strSupp = LEAD_INFO_JUNK_PTN.matcher(data.strSupp).replaceFirst("");
    data.strSupp = append(data.strSupp, "\n", trailInfo);
    return true;
  }
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("(")) return true;
    return super.isNotExtraApt(apt);
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      // "1045F",    "1045F",
      // "ASSIST-F", "ASSIST-F",
      // "BURN-F",   "BURN-F",
      // "FFIRA",    "FFIRA",
      // "FIREPD",   "FIREPD",
      // "HAZODR",   "HAZODR",
      // "MEDALS",   "MEDALS",
      // "PUMP",     "PUMP",
      "ALARMF",   "FIRE ALARM",
      "ALMCO",    "FIRE ALARM CO",
      "ALMCOM",   "FIRE ALARM CO",
      "ALMRES",   "FIRE ALARM RES",
      "FA",       "FIRE ALARM",
      "FALARM",   "FIRE ALARM",
      "FALCO",    "FIRE ALARM CO",
      "FARCG",    "ARCING WIRES",
      "FASSI",    "PROVIDE ASSISTANCE",
      "FBARB",    "BARBEQUE EMERGENCY",
      "FBLDG",    "BUILDING COLLAPSE",
      "FBRSH",    "FIRE BRUSH",
      "FBRUSH",   "FIRE BRUSH",
      "FCARB",    "CARBON MONOXIDE INCIDENT",
      "FDIVE",    "WATER RESCUE",
      "FDPLANE",  "FIRE AIRCRAFT",
      "FFIRA",    "FIRE ALARM",
      "FGAS",     "FIRE GAS LEAK",
      "FGASL",    "NATURAL GAS EMERGENCY",
      "FHAZM",    "HAZARDOUS MATERIALS INCIDENT",
      "FINVEST",  "FIRE INVESTIGATION",
      "FIRE",     "FIRE",
      "FLZ",      "FIRE LANDING ZONE",
      "FMA",      "FIRE MUTUAL AID",
      "FMUTA",    "MUTUAL AID RESPONSE",
      "FOTHER",   "FIRE OTHER",
      "FPOWR",    "POWER LINE DOWN",
      "FRESC",    "RESCUE",
      "FROLL",    "VEHICLE ROLLOVER",
      "FSMKCOND", "SMOKE CONDITION",
      "FSTRF",    "FIRE/REPORTED FIRE",
      "FSTRUC",   "STRUCTURE FIRE",
      "FSTRUCT",  "FIRE STRUCTURE",
      "FTRIB",    "TRI-BORO ALARM",
      "FTRIB-O",  "TRI-BORO RESPONSE",
      "FUEL",     "FUEL SPILL",   
      "FVEHF",    "VEHICLE FIRE",
      "FVEHICLE", "FIRE VEHICLE",
      "FWORK",    "WORKING FIRE (CONFIRMED)",
      "MA",       "MUTUAL AID",
      "MDSTB",    "MEDICAL STAND BY",
      "MED",      "MEDICAL",
      "MEDEM",    "MEDICAL EMERGENCY",
      "MEDMA",    "MEDICAL MUTUAL AID",
      "MGAS",     "EMS GAS LEAK",
      "MVA",      "MOTOR VEHICLE ACCIDENT",
      "MVA-F",    "MV CRASH W/INJURY",
      "MVAI",     "MOTOR VEHICLE ACCIDENT INJURIES",
      "MVAINJ",   "MV CRASH W/INJURY",
      "MVCIEMS",  "MV CRASH W/INJURY",
      "MVCIFD",   "MV CRASH W/INJURY",
      "PUBAST",   "PUBLIC ASSIST",
      "PUMPOUT",  "PUMPOUT",
      "SB",       "STANDBY",
      "STRUCT",   "STRUCTURE FIRE",
      "TRANSF",   "TRANSFER",

  });
}
