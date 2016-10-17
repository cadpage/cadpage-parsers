package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SmartAddressParser;



public class PAMonroeCountyParser extends SmartAddressParser {
  
  public PAMonroeCountyParser() {
    super("MONROE COUNTY", "PA");
    setFieldList("SRC ID CODE CALL PLACE ADDR APT CITY X INFO");
    removeWords("ROAD", "FS", "SQ");
    setupSpecialStreets("SUNSET STRIP");
  }
  
  @Override
  public String getFilter() {
    return "emergin@monroeco911.com,12101,alert@monroe.alertpa.org,messaging@iamresponding.com,mcpaas6@rsix.roamsecure.net,no-reply@ecnalert.com";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("#\\S+  +(.*?) *CAD|([A-Z]{3,4}|Station +\\d+)");
  private static final Pattern NEWLINE_PTN = Pattern.compile(" *\n+ *");
  private static final Pattern MARKER = Pattern.compile("(?:CAD MSG[:\n]|(\\d{8})) \\*[DG] ([A-Z0-9]+) +");
  private static final Pattern FS_PTN = Pattern.compile("(FS \\d+) *@? *");
  private static final Pattern GPS_PTN = Pattern.compile("(\\(\\d{3}\\.\\d{5},\\d{3}\\.\\d{5}\\)) *(.*)");
  private static final Pattern CITY_CODE_PTN = Pattern.compile("(?<!\\b(?:ROUTE|RT|PA)) (\\d{3})(?=(?: |$))");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // See if we can find a source code in the subject
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      int ndx = 1;
      do {
        data.strSource = match.group(ndx++);
      } while (data.strSource == null);
    }

    // Strip off any prefix
    match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCallId = getOptGroup(match.group(1));
    data.strCode = match.group(2);
    String call = CALL_CODES.getProperty(data.strCode);
    if (call == null) {
      int len1 = data.strCode.length()-1;
      char chr = data.strCode.charAt(len1);
      if (chr>='A' && chr<='Z') {
        call = CALL_CODES.getProperty(data.strCode.substring(0,len1));
      }
    }
    if (call == null) call = data.strCode;
    data.strCall = call;
    body = body.substring(match.end());
    int pt = body.indexOf("\nSent by");
    if (pt >= 0) body = body.substring(0,pt).trim();
    pt = body.indexOf(" CAD MSG:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = NEWLINE_PTN.matcher(body).replaceAll(" ");
    body = body.replaceAll("//+", "/");
    
    // See if there is a leading FS station number
    match = FS_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strPlace = match.group(1);
      body = body.substring(match.end());
    }
    
    // GPS coordinate address have a special format (and no corresponding city)
    match = GPS_PTN.matcher(body);
    if (match.matches()) {
      data.strAddress = match.group(1);
      data.strSupp = match.group(2);
      return true;
    }
    
    // The standard address parser has trouble with city codes that look like route numbers :(
    // See if we can find the city code on it's own
    match = CITY_CODE_PTN.matcher(body);
    while (match.find()) {
      String city = CITY_CODES.getProperty(match.group(1));
      if (city != null) {
        String addr = body.substring(0,match.start()).trim();
        parseAddress(StartType.START_ADDR, FLAG_AT_SIGN_ONLY | FLAG_AT_BOTH | FLAG_NO_IMPLIED_APT | FLAG_ANCHOR_END, addr, data);
        data.strCity = city;
        data.strSupp = body.substring(match.end()).trim();
        break;
      }
    }
    
    // No city, treat message as a general alert
    if (data.strCity.length() == 0) {
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body;
      return true;
    }
    
    // Extract possible cross street from beginning of info field
    // They never ever include more than once cross street.  So if this parses
    // out as an intersection, we was misinterpreted
    Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, data.strSupp);
    if (res.isValid() && res.getStatus() != STATUS_INTERSECTION) {
      res.getData(data);
      data.strSupp = res.getLeft();
    }
    
    data.strSupp = stripFieldStart(data.strSupp, "@");
    data.strSupp = stripFieldStart(data.strSupp, "/");
    return true;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "101",   "Lehman",
      "201",   "Barrett",
      "202",   "Chestnuthill",
      "203",   "Coolbaugh",
      "204",   "Eldred",
      "205",   "Hamilton",
      "206",   "Jackson",
      "207",   "Middle Smithfield",
      "208",   "Paradise",
      "209",   "Pocono",
      "210",   "Polk",
      "211",   "Price",
      "212",   "Ross",
      "213",   "Smithfield",
      "214",   "Stroud",
      "215",   "Tobyhanna",
      "216",   "Tunkhannock",
      "401",   "Delaware Water Gap",
      "402",   "East Stroudsburg",
      "403",   "Mount Pocono",
      "404",   "Stroudsburg"
  });
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "F1",   "Fire & EMS Dispatch",
      "F2",   "Fire Dispatch",
      "F3",   "Fire Officers Dispatch",
      "E1",   "Acute Abdomen",
      "E2",   "Amputation",
      "E3",   "Anaphylaxis",
      "E4",   "Burns",
      "E5",   "Cardiac",
      "E6",   "Cardiac ARREST",
      "E7",   "Spinal Injury",
      "E8",   "CVA/Stroke",
      "E9",   "Diabetic",
      "E10",  "DOA",
      "E11",  "Fever",
      "E12",  "Dislocation/Fracture",
      "E13",  "GI Bleeding",
      "E14",  "Gunshot Wound",
      "E15",  "Hemmorage",
      "E16",  "Abuse",
      "E17",  "Internal",
      "E18",  "Nausea/Vomiting",
      "E19",  "Obstetrics",
      "E20",  "Overdose",
      "E21",  "Paralysis",
      "E22",  "Penetrating Wounds",
      "E23",  "Poisoning",
      "E24",  "Psychological",
      "E25",  "Respiratory ARREST",
      "E26",  "Respiratory Distress",
      "E27",  "Convulsions/Seizures",
      "E28",  "Shock",
      "E29",  "Sprain/Strain",
      "E30",  "Unconscious/Fainting",
      "E31",  "Minor Trauma",
      "E32",  "Major Trauma",
      "E33",  "Multiple Systems Trauma",
      "E34",  "Unknown/Excited Caller",
      "E35",  "Unknown/General Illness",
      "E36",  "Refused/Cancelled",
      "E37",  "MED-ALERT Alarm",
      "E45",  "Motor Vehicle Accident",
      "E45F", "Motor Vehicle Accident with Fire",
      "E45J", "Motor Vehicle Accident with Entrapment",
      "E55",  "Fire Scene Standby",
      "E82",  "Emergency Transport",
      "E83",  "Non Emergency Transport"
      
      // Unknown codes
      // E60
  });
}
