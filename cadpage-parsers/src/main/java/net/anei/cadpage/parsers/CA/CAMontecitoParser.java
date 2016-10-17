package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Montecito, CA
 */
public class CAMontecitoParser extends SmartAddressParser {
  
  public CAMontecitoParser() {
    super("MONTECITO", "CA");
    setFieldList("CALL ADDR APT TIME");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "BARKER PASS",
        "BELLA VISTA",
        "CAMINO VIEJO",
        "CANON VIEW",
        "CHINA FLAT",
        "CIMA DEL MUNDO",
        "COAST VILLAGE",
        "COLD SPRINGS",
        "EAST VALLEY",
        "EL BOSQUE",
        "EL RANCHO",
        "EUCALYPTUS HILL",
        "FERNALD POINT",
        "HOT SPRINGS",
        "LA PAZ",
        "LA VUELTA",
        "MIRAMAR BEACH",
        "OAK CREEK CANYON",
        "OLIVE MILL",
        "PACKING HOUSE",
        "ROMERO CANYON",
        "SAN LEANDRO",
        "SAN YSIDRO",
        "SANTA ANGELA",
        "SANTA ROSA",
        "SCHOOL HOUSE",
        "STONE MEADOW",
        "SYCAMORE CANYON",
        "TEN ACRE"
   );
  }
  
  @Override
  public String getFilter() {
    return "CAD@montecitofire.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern TRAIL_TIME_PTN = Pattern.compile(" +(\\d\\d:\\d\\d:\\d\\d)$");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Visicad Message")) return false;
    if (!body.startsWith("INCIDENT DISPATCH ")) return false;
    body =  body.substring(18).trim();
    
    Matcher match = TRAIL_TIME_PTN.matcher(body);
    if (match.find()) {
      data.strTime = match.group(1);
      body = body.substring(0,match.start());
    }
    
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, body, data);
    return true;
  }
  
  private static CodeSet CALL_LIST = new CodeSet(
      "ALARM - CARBON MONOXIDE",
      "ALARM - FIRE: RES, COMM",
      "ALARM - MEDICAL",
      "ANIMAL PROBLEM/RESCUE",
      "AUTOMATCI AID STB",
      "AUTOMATIC AID VNC",
      "BACK INJURY / PAIN",
      "BRUSH/VEGETATION FIRE",
      "DIABETIC PROBLEM",
      "FALL, CODE2",
      "ILL PERSON",
      "INVESTIGATION",
      "LIFT ASSIST",
      "MEDICAL, CODE 2",
      "MEDICAL EMERGENCIES",
      "NAT GAS LEAK/MAIN BREAK OUTSID",
      "PUBLIC ASSIST GENERAL",
      "STRUCTURE FIRE",
      "TEST CALL",
      "TRAFFIC COLLISION RESPONSE",
      "TRAIL RESCUE",
      "TRANSFORMER FIRE",
      "TREE DOWN",
      "UNCONSCIOUS",
      "UNKNOWN PROBLEM/ PERSON DOWN",
      "WATER PROBLEM/BROKEN MAIN",
      "WIRES DOWN"
  );
}
