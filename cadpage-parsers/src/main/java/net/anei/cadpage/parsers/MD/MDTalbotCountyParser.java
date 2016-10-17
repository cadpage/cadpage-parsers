package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDTalbotCountyParser extends SmartAddressParser {

  private static final Pattern MASTER = Pattern.compile("(?:TALBOTDES:? +)?Talbot ?911:\\*[DG] +([^ ]+) +(?:([A-E])-)?(.*?) +([A-Z]\\d+)");
  private static final Pattern MM_PTN = Pattern.compile("(@MM \\d+)\\b *(.*)");
  
  
  public MDTalbotCountyParser() {
    super("TALBOT COUNTY", "MD");
    setFieldList("BOX PRI CALL ADDR APT PLACE INFO UNIT");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "BAILEYS NECK",
        "BLACK DUCK",
        "BOONE CREEK",
        "CHARLES I BOYLE",
        "CROUSE MILL",
        "DOGWOOD COVE",
        "FOX MEADOW",
        "FOX SQUIRREL",
        "HOPKINS NECK",
        "JOHN BROWN",
        "LITTLE PARK",
        "MAPLE HALL",
        "MILLERS LANDING",
        "NELSON POINT",
        "NORTH SIXTH",
        "PIN OAK",
        "QUAIL RUN",
        "QUAKER NECK",
        "QUARTER CREEK",
        "QUEEN ANNE",
        "RUSTLING OAKS",
        "STONEY RIDGE",
        "SWANN HAVEN",
        "TILGHMAN ISLAND",
        "TUCKAHOE SPRINGS",
        "WAVERLY ISLAND",
        "WILLOUGHBY CANNER",
        "WYE MILLS"
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
    data.strUnit = match.group(4);
    
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
      "ELECTRICAL HAZARD",
      "FALLS/BACK TRAUMA",
      "GAS LEAK",
      "GENERAL FIRE ALARM",
      "HEADACHE",
      "HEART PROBLMS/AICD",
      "HEMORRHAGE/LACS",
      "MEDICAL ALARM",
      "MISC MEDICAL CALL",
      "MOTOR VEH COLLISION",
      "MVA/PI",
      "OTHER ALARM",
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
      "WALKIN AT STATION",
      "WATERFLOW ALARM"
  );
}
