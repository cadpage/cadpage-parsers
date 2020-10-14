package net.anei.cadpage.parsers.AR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARPulaskiCountyCParser extends SmartAddressParser {
  
  public ARPulaskiCountyCParser() {
    super("PULASKI COUNTY", "AR");
    setFieldList("ID CALL ADDR PLACE APT CITY ST CH");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(ARPulaskiCountyAParser.MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "smtp@pcso.org";
  }
  
  private static final Pattern ID_PTN = Pattern.compile("CFS\\d{4}-\\d{5}");
  private static final Pattern MASTER = Pattern.compile("([^,]+)(?:, *([A-Z ]+))??(?:, *([A-Z]{2})(?: (\\d{5}))?)?(?: ([ A-Z0-9]*?))?");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!ID_PTN.matcher(subject).matches()) return false;
    data.strCallId = subject;
    
    int pt = body.indexOf(" Fire Channel");
    if (pt >= 0) {
      data.strChannel = body.substring(pt+13).trim();
      body = stripFieldEnd(body.substring(0,pt), "Respond");
    } else return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(StartType.START_CALL, FLAG_RECHECK_APT | FLAG_ANCHOR_END, match.group(1).trim(), data);
    data.strCity = getOptGroup(match.group(2));
    data.strState = getOptGroup(match.group(3));
    String zip = match.group(4);
    if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    data.strChannel = append(data.strChannel, "/", getOptGroup(match.group(5)));
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT INJURY",
      "CHEMICAL SPILL",
      "FALL",
      "FIRE GRASS/WOODS",
      "FIRE OTHER STRUCTURE",
      "FIRE RESIDENTIAL",
      "FIRE VEHICLE",
      "ILLEGAL BURN",
      "MEDICAL EMERGENCY",
      "NATURAL DISASTER",
      "SHOOTING",
      "SUICIDE ATTEMPT",
      "Suicide Threats",
      "SUSPICIOUS PERSON",
      "WELFARE CONCERN"
  );
}
