package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MDQueenAnnesCountyParser extends SmartAddressParser {
  
  private static final Pattern PREFIX_PTN = Pattern.compile("^(?:qac911|QA911com):");
  private static final Pattern MARKER = Pattern.compile("^(?:(?:qac911|QA911com):\\*)?[DG] ");
  private static final Pattern UNIT_PTN = Pattern.compile(" +([A-Z]{1,2}\\d{2})$");
  private static final Pattern BOX_PTN = Pattern.compile("(\\d{1,2}-\\d{1,2}) (.*?)((?: +(?:COMMERCIAL|MEDICAL|STILL|WATER RESCUE|RESCUE))?(?: +LOCAL)?)(?: +BOX)?(?: (Q\\d{2}))?");
  private static final Pattern PAREN_PTN = Pattern.compile(" *\\((.*?)\\) *");
  private static final Pattern MA_CALL_PTN = Pattern.compile("([A-Z]{4}) +MUTUAL AID\\b.*");
  
  public MDQueenAnnesCountyParser() {
    super("QUEEN ANNES COUNTY", "MD");
    setFieldList("BOX CALL CITY ADDR APT PLACE INFO CH UNIT");
    setupCallList(CALL_LIST);
    addNauticalTerms();
    removeWords("NEW");
    setupMultiWordStreets(
        "BATTS NECK",
        "BAY BRIDGE",
        "BAY CITY",
        "BENNETT POINT",
        "BUSCHS FRONTAGE",
        "CASTLE HARBOR",
        "CASTLE MARINA",
        "CHANNEL MARKER",
        "CHESTER RIVER BEACH",
        "CHESTER RIVER",
        "CHESTER STATION",
        "CHESTERVILLE BRIDGE",
        "CHEWS MANOR",
        "CHURCH HILL",
        "CLABBER HILL",
        "CLAIBORNE FIELDS",
        "COX NECK",
        "COX SAWMILL",
        "CRAB ALLEY",
        "CREEKSIDE COMMONS",
        "DOUBLE CREEK POINT",
        "DUCK PUDDLE",
        "DULIN CLARK",
        "FLAT IRON SQUARE",
        "GOLDEN EYE",
        "GRANNY BRANCH",
        "GRASONVILLE CEMETERY",
        "GREAT NECK",
        "HESS FRONTAGE",
        "HICKORY RIDGE",
        "HIGH BRIDGE",
        "HOUGHTON HOUSE",
        "HOUSE POINT",
        "JOHN BROWN",
        "JOHN PATRICK",
        "KENT MANOR",
        "KENT NARROWS",
        "KING STORE",
        "LITTLE CREEK",
        "LITTLE KIDWELL",
        "LITTLE NECK",
        "LOG CANOE",
        "LONG POINT",
        "LOVE POINT",
        "MACUM CREEK",
        "MARION QUIMBY",
        "MONROE MANOR",
        "NICHOLS MANOR",
        "OLD LOVE POINT",
        "OUTLET CENTER",
        "OYSTER COVE",
        "PERRYS CORNER",
        "PINE COVE",
        "PINEY CREEK",
        "PINEY NARROWS",
        "PRICE STATION",
        "PROSPECT BAY",
        "QUAKER NECK",
        "QUEEN ANNE",
        "QUEEN MARY",
        "QUEEN NEVA",
        "QUEEN VICTORIA",
        "RABBIT HILL",
        "RED LION BRANCH",
        "RIVER VIEW",
        "ROLLING BRIDGE",
        "SAYERS FOREST",
        "SCHOOL HOUSE",
        "SHOPPING CENTER",
        "THOMPSON CREEK",
        "WEB FOOT",
        "WELLS COVE",
        "WHITE HOUSE",
        "WHITE MARSH",
        "WINDSWEPT FARM",
        "WOODS EDGE",
        "WYE HARBOR",
        "WYE MILLS"
    );
  }
  
  @Override
  public String getFilter() {
    return "qac911@qac.org,QA911com@qac.org,@c-msg.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // See if part of message has been split into subject
    if (subject.startsWith("*") && subject.endsWith("DUE")) {
      Matcher match = PREFIX_PTN.matcher(body);
      int pt = 0;
      if (match.lookingAt()) pt = match.end();
      body = body.substring(0,pt) +  subject + ':' + body.substring(pt);
    }

    Matcher match = MARKER.matcher(body);
    if (match.find()) body = body.substring(match.end());
    
    // Strip UNIT from end of text
    int pt = body.lastIndexOf("DUE:");
    if (pt >= 0) {
      data.strUnit = body.substring(pt+4).trim();
      body = body.substring(0,pt).trim();
    } else {
      match = UNIT_PTN.matcher(body);
      if (match.find()) {
        data.strUnit = match.group(1);
        body = body.substring(0, match.start()).trim();
      }
    }
    
    // Strip box number from front of message and box description from end
    match = BOX_PTN.matcher(body);
    if (match.matches()) {
      data.strBox = append(match.group(3).trim(), " ", match.group(1));
      body = match.group(2).trim();
      data.strChannel = getOptGroup(match.group(4));
    }
    
    // Sometimes they put valid street names in parenthesis, which messes up
    // the address detection logic.
    match = PAREN_PTN.matcher(body);
    if (match.find() && isValidAddress(match.group(1))) {
      body = body.substring(0,match.start()) + ' ' + match.group(1) + ' ' + body.substring(match.end());
      body = body.trim();
    }
    
    // For several reasons,the FLAG_AT_BOTH logic doesn't quite work, so we
    // will have to check for an @ and do the had work ourselves
    pt = body.indexOf('@');
    if (pt < 0) {
      
      // No @ -  If there is a TRANSFER/COVER tag, it marks the end of the address 
      pt = body.indexOf("TRANSFER/COVER");
      if (pt >= 0) {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_NO_IMPLIED_APT | FLAG_ANCHOR_END, body.substring(0,pt).trim(), data);
        data.strSupp = body.substring(pt).trim();
      } else {
        parseAddress(StartType.START_CALL_PLACE, FLAG_START_FLD_REQ | FLAG_NO_IMPLIED_APT , body, data);
        data.strSupp = getLeft();
      }
    } 
    
    else {
      
      // There is an @ - Split message body by the first @ and try to parser
      // both sides as an address
      String part1 = body.substring(0,pt).trim();
      String part2 = body.substring(pt+1).trim();
      Result res1 = parseAddress(StartType.START_CALL_PLACE, FLAG_START_FLD_REQ | FLAG_IGNORE_AT | FLAG_OPT_STREET_SFX | FLAG_NO_IMPLIED_APT | FLAG_ANCHOR_END, part1);
      Result res2 = parseAddress(StartType.START_ADDR, FLAG_IGNORE_AT | FLAG_OPT_STREET_SFX | FLAG_NO_IMPLIED_APT, part2);
      if (res2.getStatus() > res1.getStatus()) {
        
        // Second part is the address
        // Split first part into call and place name
        res2.getData(data);
        data.strSupp = res2.getLeft();
        String call = CALL_LIST.getCode(part1, true);
        if (call == null) {
          data.strCall = part1;
        } else {
          data.strCall = call;
          data.strPlace = part1.substring(call.length()).trim();
        }
      }
      
      else {
        
        // First part is address
        // Entire second part is place name
        res1.getData(data);
        if (data.strAddress.length() == 0) data.strAddress = data.strPlace;
        data.strPlace = part2;
      }
    }
    
    // See if this is a mutual aid call that we can extract a city name from
    match = MA_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      String city = MA_CITY_TABLE.getProperty(match.group(1));
      if (city != null) data.strCity = city;
    }
    
    // If not address was found, move place to address
    if (data.strAddress.length() == 0) {
      String addr = data.strPlace;
      data.strPlace = "";
      parseAddress(addr, data);
    }

    // Box is required, unless this was a mutual aid call
    return (data.strBox.length() > 0 || data.strCall.contains("MUTUAL AID"));
  }
  
  private static CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAINS",
      "ALERT",
      "ALLERGIC/REACTION",
      "ANIMAL BITE/ATTACK",
      "APPLIANCE FIRE",
      "ASSAULT",
      "BACK PAIN-NONTRAUMA",
      "BREATHING PROBLEMS",
      "BRUSH/GRASS FIRE",
      "CARDIAC ARREST",
      "CHEST PAINS",
      "CHIMNEY FIRE",
      "CHIMNEY FIRE",
      "CHOKING",
      "CITIZEN ASSIST",
      "CO ALARM",
      "COLD EXPOSURE",
      "COMMERCIAL BLDG FIRE",
      "COMMERCIAL VEH FIRE",
      "DIABETIC PROBLEMS",
      "DROWNING/DIVE ACCDNT",
      "DWELLING FIRE",
      "ELECTRICAL HAZARD",
      "ELEVATOR MALFUNCTION",
      "EYE PROBLEM/INJURY",
      "EXPECTED DEATH",
      "FAINTING",
      "FALLS",
      "FIRE ALARM",
      "FIRE TEST",
      "FIRE UNIT TRANSFER",
      "FUEL SPILL IN DRAIN",
      "GENERAL FIRE ALARM",
      "HAZMAT",                                                                                                                                                                                                                                                
      "HAZMAT/SMALL SPILL",
      "HEADACHE",
      "HEART PROBLEMS", 
      "HEM/LACERATION",
      "HEMORRHAGE/LACS",
      "INTENTIONAL OVERDOSE",
      "INSIDE GAS LEAK",
      "LG BRUSH/GRASS FIRE",
      "LOCK OUT OF VEHICLE",
      "LOCK OUT",
      "MLTPL DWELLING FIRE",
      "MVC/NOT ALERT",
      "MVC INVOLVING A BUS",
      "MVC UNKNOWN INJURIES",
      "MVC W/ENTRAPMENT",
      "MVC W/INJURIES",
      "MVC W/MINOR INJURIES",
      "MVC W/PEDESTRIAN",
      "MVC W/ROLLOVER",
      "NEAR FAINTING",  
      "OBVIOUS DEATH",
      "ODOR OF GAS INSIDE",
      "ODOR OF SMOKE INSIDE",
      "ODOR OF GAS OUTSIDE",
      "OUTSIDE FIRE",
      "OUTSIDE GAS LEAK",
      "OUTSIDE ODOR OF GAS",
      "PENETRATING TRAUMA",
      "PSYCHIATRIC",
      "PSYCHIATRIC/SUICIDE",
      "SCUBA DIVE ACCIDENT",
      "SEIZURE",
      "SEIZURES",
      "SERVICE CALL",
      "SICK PERSON",
      "SMALL FUEL SPILL",
      "SMALL GRASS FIRE",
      "SMALL OUTSIDE FIRE",
      "SMALL STRUCTURE FIRE",
      "SMOKE DETECTOR",
      "SMOKE INVESTIGATION",
      "STROKE",
      "STROKE (CVA)",
      "STROKE(CVA)<2HRS",
      "STROKE(CVA)>2HRS",
      "STRUCTURE FIRE/OUT",
      "TRAILER FIRE",
      "TRAUMATIC INJURY",
      "UNCONSCIOUS",                                                                                                                                                                                                                                           
      "UNCONSCIOUS/FAINTING",
      "UNKNOWN PROBLEM",
      "VEH FIRE W/EXPOSURE",
      "VESSEL TAKING WATER",
      "WATER RESCUE",
      "WILDLAND FIRE DAVIDSON",                                                                                                                                                                                                                                
      "WIRES DOWN",
      "VEHICLE FIRE",
      
      "AACO MUTUAL AID",
      "CARO MUTUAL AID",
      "KENT MUTUAL AID",
      "KENT MUTUAL AID MEDICAL",
      "KM06 MUTUAL AID MEDICAL",
      "MUTUAL AID",
      "OTHE MUTUAL AID"
  );
  
  private static final Properties MA_CITY_TABLE = buildCodeTable(new String[]{
      "AACO", "ANNE ARUNDEL COUNTY",
      "CARO", "CAROLINE COUNTY",
      "KENT", "KENT",
  });
}
