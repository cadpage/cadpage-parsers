package net.anei.cadpage.parsers.AZ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class AZNavajoCountyCParser extends SmartAddressParser {
  
  public AZNavajoCountyCParser() {
    super("NAVAJO COUNTY", "AZ");
    setupCallList(CALL_LIST);
    setFieldList("UNIT CH CALL ADDR APT INFO");
  }
  
  private static final Pattern MASTER = Pattern.compile("((?:[A-Z]+\\d+ )+)ch *(\\d+) +(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern SPACE_PTN = Pattern.compile("SPACE +(\\S+) *(.*)", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strUnit = match.group(1).trim();
    data.strChannel = match.group(2);
    parseAddress(StartType.START_CALL_PLACE, FLAG_START_FLD_REQ, match.group(3), data);
    String left = getLeft();
    match = SPACE_PTN.matcher(left);
    if (match.matches()) {
      data.strApt = append(data.strApt, "-", match.group(1));
      left = match.group(2);
    }
    data.strSupp = left;
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ABD PAIN",
      "CHECK WELFARE",
      "CHK WELFARE",
      "DIABETIC PROBLEM"
  );
}
