package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDTalbotCountyAParser extends SmartAddressParser {

  private static final Pattern MASTER = Pattern.compile("(?:TALBOTDES:? +)?Talbot ?911:\\*[DG] +([^ ]+) +(?:([A-E])-)?(.*?)(?: +([A-Z]\\d+|TC))?");
  private static final Pattern MM_PTN = Pattern.compile("(@MM \\d+)\\b *(.*)");
  
  
  public MDTalbotCountyAParser() {
    super("TALBOT COUNTY", "MD");
    setFieldList("BOX PRI CALL ADDR APT PLACE INFO UNIT");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "ALL WAYS IN",
        "BAILEYS NECK",
        "BLACK DUCK",
        "BLUE HERON",
        "BOONE CREEK",
        "CHARLES I BOYLE",
        "COUNTRY CLUB",
        "CROUSE MILL",
        "DOGWOOD COVE",
        "EASTON CLUB",
        "EASTON VILLAGE",
        "FERRY COVE",
        "FOX MEADOW",
        "FOX SQUIRREL",
        "GRANGE HALL",
        "GRAUSE MILL",
        "HIGH BANKS",
        "HOPKINS NECK",
        "JOHN BROWN",
        "KINGDALE FARM",
        "KITTYS CORNER",
        "LANDING NECK",
        "LEEDS LANDING",
        "LITTLE PARK",
        "MAPLE HALL",
        "MILLERS LANDING",
        "MORGANS POINT",
        "NELSON POINT",
        "NORTH LIBERTY",
        "NORTH SIXTH",
        "PIN OAK",
        "QUAIL RUN",
        "QUAKER NECK",
        "QUARTER CREEK",
        "QUEEN ANNE",
        "RABBIT HILL",
        "RUSTLING OAKS",
        "SHADY OAK",
        "SKIPTON LANDING",
        "ST MICHAELS",
        "STONEY RIDGE",
        "SWANN HAVEN",
        "THREE BRIDGE BRANC",
        "TILGHMAN ISLAND",
        "TUCKAHOE SPRINGS",
        "TUNIS MILLS",
        "WAVERLY ISLAND",
        "WILLOUGHBY CANNER",
        "WYE MILLS",
        "YACHT CLUB"
    );
  }
  
  @Override
  public String getFilter() {
    return "msg@cfmsg.com,alert@cfmsg.com,Talbot911@talbotdes.org,777,4702193527";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    int pt = body.indexOf(" Text UNSUB");
    if (pt >= 0) body = body.substring(0,pt).trim();
    pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    body = body.substring(match.end()).trim();
    data.strBox = match.group(1);
    data.strPriority = getOptGroup(match.group(2));
    String addr = match.group(3).trim();
    data.strUnit = getOptGroup(match.group(4));
    
    // An @ marks the beginning of a place name, but since we have not way to determine the end of the 
    // place name, we will dump everything into extra info
    int flags = FLAG_START_FLD_REQ | FLAG_IGNORE_AT;
    pt = addr.indexOf('@');
    if (pt >= 0) {
      data.strSupp = addr.substring(pt);  // include the @
      addr = addr.substring(0,pt).trim();
      flags |= FLAG_ANCHOR_END;
    }
    
    // OK, go do your magic!!
    parseAddress(StartType.START_CALL, flags, addr, data);
    if (data.strSupp.length() == 0) data.strSupp = getLeft();
    data.strSupp = data.strSupp.replaceAll("  +", " ");
    
    // A MM construct in info goes back to the address
    match = MM_PTN.matcher(data.strSupp);
    if (match.matches()) {
      data.strAddress = append(data.strAddress, " ", match.group(1));
      data.strSupp = match.group(2);
    }
    
    // If unit was not found, merge box into call description
    if (data.strUnit.length() == 0) {
      data.strCall = append(data.strBox, " ", data.strCall);
      data.strBox = "";
    }
    
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ALLERGIES/ENVENOMA",
      "ASSIST EMS",
      "ASSIST FIRE",
      "BACK PAIN",
      "BREATHING PROBLEMS",
      "CARDIAC/RESP ARRES",
      "CHEST PAIN",
      "CHIMNEY FIRE",
      "CONVULSIONS/SEIZUR",
      "DIABETIC PROBLEMS",
      "ELECTRICAL HAZARD",
      "FALLS/BACK TRAUMA",
      "GAS LEAK",
      "GENERAL FIRE ALARM",
      "HEADACHE",
      "HEART PROBLMS/AICD",
      "HEMORRHAGE/LACS",
      "LG NON-DWELLING FIRE",
      "MEDICAL ALARM",
      "MISC FIRE",
      "MISC MEDICAL CALL",
      "MOTOR VEH COLLISION",
      "MVA/PI",
      "MVC - PEDESTRIAN",
      "OTHER ALARM",
      "OUTSIDE FIRE",
      "OVERDOSE/POISONING",
      "POLICE REQUEST EMS",
      "PSYCHIATRIC/SUICID",
      "RESIDENTIAL FIRE",
      "SICK PERSON",
      "SMALL OUTSIDE FIRE",
      "SM NON-DWELLING FIRE",
      "SMOKE DETECTOR",
      "SMOKE INVESTIGATE",
      "STROKE (CVA)",
      "UNCONSCIOUS/FAINT",
      "VEHICLE FIRE",
      "WALKIN AT STATION",
      "WATER RESCUE",
      "WATERFLOW ALARM"
  );
}
