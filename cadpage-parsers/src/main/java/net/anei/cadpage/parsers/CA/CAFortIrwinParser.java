package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class CAFortIrwinParser extends SmartAddressParser {

  public CAFortIrwinParser() {
    super("FORT IRWIN", "CA");
    setFieldList("CALL UNIT ADDR X APT INFO");
    setupMultiWordStreets("GRANITE PASS", "LANGFORD LAKE", "RED PASS", "SOUTH LOOP");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static Pattern UNIT_TO_ADDR = Pattern.compile("(.+?) +(?:is |are )?respond(?:ing|ed)(?: to(?: (?:the|a))?) +(.*)");
  private static Pattern AND = Pattern.compile("(?: +AND +| *, *)", Pattern.CASE_INSENSITIVE);
  private static Pattern AT_PTN1 = Pattern.compile(" *@ *");
  private static Pattern AT_PTN2 = Pattern.compile(" +at +");
  private static Pattern GENERIC_BUILDING_CODE = Pattern.compile("(B\\d+(?: ?[A-Z])?|(?:DFAC|BUILDING|BARRACKS|BLDG) *\\d+|LANDMARK INN)\\b[,\\.]? *(.*?)$", Pattern.CASE_INSENSITIVE);
  private static Pattern NOT_ADDRESS_PTN = Pattern.compile("\\d+ +(?:YEAR|YO|Y/O) .*", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    // Save subject as call
    if (subject.length() == 0) return false;
    data.strCall = subject;

    // Remove disclaimer and dispatch information
    int ni = body.indexOf("\n");
    if (ni >= 0) body = body.substring(0, ni);

    // Attempt to parse "responding to" format
    Matcher utaMat = UNIT_TO_ADDR.matcher(body);
    if (utaMat.matches()) {
      
      // If we found that marker, we are positively going treat this as a 
      // dispatch alert, even if we can not find an address
      data.strUnit = AND.matcher(utaMat.group(1).trim()).replaceAll(" ");
      body = utaMat.group(2);
      
      // First see if we can parse an address or building from beginning of text
      // This is only allowed if have found a unit at the beginning of the text
      if (parseGenericBuildingCodeOrAddress(true, body, data)) return true;
      
      // If that doesn't work, look for an @ or AT marker and see if it is
      // followed by an identifiable address
      Matcher match = AT_PTN1.matcher(body);
      boolean found = match.find();
      if (!found) {
        match = AT_PTN2.matcher(body);
        found = match.find();
      }
      
      if (found) {
        String start = body.substring(0,match.start());
        String left = body.substring(match.end());
        if (parseGenericBuildingCodeOrAddress(true, left, data)) {
          data.strSupp = append(start, " / ", data.strSupp);
          return true;
        }
        data.strSupp = start;
        parseAddress(left, data);
        return true;
      }
      
      if (parseGenericBuildingCodeOrAddress(false, body, data)) return true;
      
      data.strSupp = body;
      return true;
    }
    
    // If we did not find the responding marker, we will still accept this
    // as a dispatch alert, but only if we find an @ followed by an address
    Matcher match = AT_PTN1.matcher(body);
    if (match.find()) {
      String  start = body.substring(0,match.start());
      String  left = body.substring(match.end());
      if (parseGenericBuildingCodeOrAddress(true, left, data)) {
        data.strSupp = append(start, " / ", data.strSupp);
        return true;
      }
    }

    // Otherwise, make it a general alert
    data.strCall = "GENERAL ALERT";
    data.strPlace = "(" + subject + ") " + body;
    data.strUnit = "";
    return true;
  }

  /**
   * parse address or building name from text line 
   * @param lockStart true if address/building name must occure at beginning of line
   * @param body text to be parsed
   * @param data data object in which parsed data is returned
   * @return true if successful, false otherwise
   */
  private boolean parseGenericBuildingCodeOrAddress(boolean lockStart, String body, Data data) {
    Matcher gbcMat = GENERIC_BUILDING_CODE.matcher(body);
    boolean match = lockStart ? gbcMat.matches() : gbcMat.find();
    if (match) {
      
      data.strAddress = gbcMat.group(1);
      data.strSupp = body.substring(0,gbcMat.start()).trim();
      
      // Check for cross road after code
      String left = gbcMat.group(2);
      Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, left);
      if (res.isValid()) {
        left = res.getLeft();
        res.getData(data);
      } 
      data.strSupp = append(data.strSupp, " / ", left);
      return true;
    }

    // Try a parseAddress requiring street suffixes second
    // If that doesn't work try not requiring street suffixes
    // But make some special checks to make sure we do not treat
    // an pt age as a unsuffixed address
    StartType st = lockStart ? StartType.START_ADDR : StartType.START_OTHER;
    Result res = parseAddress(st, body);
    boolean good = res.isValid();
    if (!good) {
      if (!NOT_ADDRESS_PTN.matcher(body).matches()) {
        res = parseAddress(StartType.START_ADDR, FLAG_OPT_STREET_SFX, body);
        good = res.getStatus() >= STATUS_FULL_ADDRESS;
      }
    }
    if (good) {
      res.getData(data);
      data.strSupp = res.getLeft();
      data.strSupp = append(res.getStart(), " / ", res.getLeft());
      Matcher m = ADDR_AT_PTN.matcher(data.strAddress);
      if (m.matches()) {
        data.strSupp = m.group(1).trim();
        data.strAddress = m.group(2);
      }
      return true;
    }
    return false;
  }
  
  private static final Pattern ADDR_AT_PTN = Pattern.compile("(.*)\\bAT +(.*)",Pattern.CASE_INSENSITIVE);
}
