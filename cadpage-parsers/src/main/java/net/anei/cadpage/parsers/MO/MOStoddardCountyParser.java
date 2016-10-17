package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class MOStoddardCountyParser extends SmartAddressParser {
  
  public MOStoddardCountyParser() {
    super(CITY_LIST, "STODDARD COUNTY", "MO");
    setFieldList("CALL PRI PLACE ADDR APT CITY X SRC UNIT");
  }
  
  private static final Pattern MASTER = Pattern.compile("(.*?) (?:PRIORITY (\\S+)|NON-PRIORITY) +(.*?) CrossStreets: *(.*)\nESN: *(\\S+) *\n(.*)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Dispatch Call")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    data.strPriority = match.group(2);
    if (data.strPriority == null) data.strPriority = "NONE";
    parseAddress(StartType.START_PLACE, FLAG_FALLBACK_ADDR | FLAG_ANCHOR_END, match.group(3).trim(), data);
    data.strCross = match.group(4).trim();
    data.strSource = match.group(5);
    data.strUnit = match.group(6).trim();
    return true;
  }
  
  private static final String[] CITY_LIST = new  String[]{
    "ADVANCE",
    "BAKER",
    "BELL CITY",
    "BERNIE",
    "BLOOMFIELD",
    "BROWNWOOD",
    "CASTOR",
    "DEXTER",
    "DUCK CREEK",
    "DUDLEY",
    "ELK",
    "ESSEX",
    "GRAYRIDGE",
    "GUAM",
    "IDALIA",
    "LEORA",
    "LIBERTY",
    "NEW LISBON",
    "PAINTON",
    "PENERMON",
    "PIKE",
    "POWE",
    "PUXICO",
    "RICHLAND",
    "STODDARD COUNTY"
  };
}
