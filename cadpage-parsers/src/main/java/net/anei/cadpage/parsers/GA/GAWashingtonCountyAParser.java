package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class GAWashingtonCountyAParser extends SmartAddressParser {
  
  public GAWashingtonCountyAParser() {
    super("WASHINGTON COUNTY", "GA");
    setupCallList(CALL_LIST);
    setFieldList("CODE CALL ADDR PHONE APT X");
  }
  
  @Override
  public String getFilter() {
    return "WASHINGTONCO911@911.com";
  }
  
  private static final Pattern MASTER = Pattern.compile("WASHINGTONCO911:(\\S+) (.*) (\\d{10})(?: Apt:(.*) Bldg)?\\b *(.*)");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCode = match.group(1);
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, match.group(2).trim(), data);
    data.strPhone = match.group(3);
    data.strApt = append(data.strApt, "-", getOptGroup(match.group(4)));
    data.strCross = match.group(5);
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "EMS OTHER",
      "FALL",
      "SICK PERSON"
  );
}
