package net.anei.cadpage.parsers.OK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class OKYukonParser extends SmartAddressParser {
  public OKYukonParser() {
    super("YUKON", "OK");
    setFieldList("CALL ADDR APT PLACE ID CITY X");
  }

  @Override
  public String getFilter() {
    return "911dispatch@cityofyukonok.gov";
  }
  
  static private final Pattern MASTER_PATTERN
    = Pattern.compile("(.*)  ([A-Z][a-z]+(?: +[A-Z][a-z]+)?)(.*)");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    Matcher m=MASTER_PATTERN.matcher(body);
    if (m.matches()) {
      parseCallAddrId(m.group(1).trim(), data);
      data.strCity=m.group(2).toUpperCase();
      data.strCross=m.group(3).trim();
    }
    else
      parseCallAddrId(body, data);
    return true;
  }
  
  static private final Pattern CALL_ADDR_ID_PATTERN
    = Pattern.compile("(.*)\\b(\\d{4}-\\d{8})");
  private void parseCallAddrId(String field, Data data) {
    Matcher m=CALL_ADDR_ID_PATTERN.matcher(field);
    if (m.matches()) {
      field = m.group(1).trim();
      data.strCallId = m.group(2);
    }
    parseAddress(StartType.START_CALL, FLAG_RECHECK_APT, field, data);
    data.strPlace = getLeft().trim();
  }
}
