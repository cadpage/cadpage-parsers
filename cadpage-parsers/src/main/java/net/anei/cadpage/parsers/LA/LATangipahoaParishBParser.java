package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class LATangipahoaParishBParser extends SmartAddressParser {
  
  public LATangipahoaParishBParser() {
    super("TANGIPAHOA PARISH", "LA");
    setFieldList("SRC CALL ADDR APT CITY ST");
  }
  
  @Override
  public String getFilter() {
    return "HFD@hammond.org";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("[A-Z]{1,3}FD");
  private static final Pattern MASTER = Pattern.compile("(.*?), (?:([A-Za-z ]+), )?([A-Z]{2})");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!SUBJECT_PTN.matcher(subject).matches()) return false;
    data.strSource = subject;
    
    body = body.replace("Hamm0nd", "Hammond");
    body = body.replace(" La.,", ",");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, match.group(1).trim(), data);
    data.strCity = getOptGroup(match.group(2));
    data.strState = match.group(3);
    return true;
  }

}
