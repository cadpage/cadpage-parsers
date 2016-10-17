package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NCIredellCountyParser extends MsgParser {
  
  private static final Pattern MASTER_PTN = Pattern.compile("((?:A|FA|FD|FM|FR)\\d+?(?=[\\.| A-Z]|10-)|MFD|MRS|ICRS|SFD|NIRS)[-\\. ]*+([^,]+?), *([^,]*)(?:, *(\\d{2}-\\d{5,6}))?");
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(?:(C-\\d+) |(\\d*)-|([A-Z]+) - )(.*)");
  
  public NCIredellCountyParser() {
    super("IREDELL COUNTY", "NC");
    setFieldList("UNIT CODE CALL ADDR ID");
  }
  
  @Override
  public String getFilter() {
    return "CommtechMessenger,@co.iredell.nc.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = body.replace("\n", "");
    Matcher match = MASTER_PTN.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = getOptGroup(match.group(1));
    String call = match.group(2).trim();
    parseAddress(match.group(3).replace('$', '&'), data);
    data.strCallId = getOptGroup(match.group(4));
    
    call = convertCodes(call, CALL_TABLE);
    match = CODE_CALL_PTN.matcher(call);
    if (match.matches()) {
      int cnt = match.groupCount();
      for (int ii = 1; ii<cnt; ii++) {
        String code = match.group(ii);
        if (code != null) {
          data.strCode = code;
          break;
        }
      }
      call = match.group(cnt);
    }
    data.strCall = call;
    return data.strAddress.length() > 0;
  }
  
  // Usually a dash separates a call code and call.  But there are some
  // unfortunate exceptions.
  private static final Properties CALL_TABLE = buildCodeTable(new String[]{
      "CARBON - MONOXIDE ACTIVATION", "CARBON MONOXIDE ACTIVATION",
      "ILLEGAL - BURN",      "ILLEGAL BURN",
      "PUBLIC - SERVICE",    "PUBLIC SERVICE"
      
  });
}
