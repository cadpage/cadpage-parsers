package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class MILeelanauCountyAParser extends DispatchA19Parser {

  public MILeelanauCountyAParser() {
    super(MILeelanauCountyParser.CITY_CODES, "LEELANAU COUNTY", "MI");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Rapid Notifications")) return false;
    if (!super.parseMsg("", body, data)) return false;

    int callBrkPos = data.strCall.indexOf(" - ");
    if (callBrkPos < 0) callBrkPos = data.strCall.length();
    data.strCall = convertCodes(data.strCall.substring(0,callBrkPos), MILeelanauCountyParser.CALL_CODES) + data.strCall.substring(callBrkPos);
    return true;
  }

  @Override
  public String getFilter() {
    return "outgoing@co.leelanau.mi.us,outgoing@leelanau.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
