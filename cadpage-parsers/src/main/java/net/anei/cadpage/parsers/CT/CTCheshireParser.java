package net.anei.cadpage.parsers.CT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class CTCheshireParser extends SmartAddressParser {

  public CTCheshireParser() {
    super(CITY_LIST, "CHESHIRE", "CT");
    setFieldList("UNIT ADDR APT CITY CALL ID");
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    // check subj
    if (!subject.equals("CFD Dispatch")) return false;

    // parse as general alert if normal parsing fails
    if (!parseAddressInternal(body, data)) data.parseGeneralAlert(this, body);

    // never fail
    return true;
  }

  private static final Pattern MASTER = Pattern.compile("(?:([ A-Z0-9]*?)  )?(.*?)(?: (\\d{4}-\\d{8}))?");
  
  private boolean parseAddressInternal(String body, Data data) {
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strUnit = getOptGroup(match.group(1));
    String addr = match.group(2).trim();
    data.strCallId = getOptGroup(match.group(3));
    if (data.strUnit.length() == 0 && data.strCallId.length() == 0) return false;
    
    parseAddress(StartType.START_ADDR, addr, data);
    data.strCall = getLeft();
    return data.strCall.length() > 0;
  }
  
  private static String[] CITY_LIST = new String[]{
    "CHESHIRE"
  };
}
