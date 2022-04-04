package net.anei.cadpage.parsers.NH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class NHGraftonCountyCParser extends DispatchA27Parser {

  public NHGraftonCountyCParser() {
    super("GRAFTON COUNTY", "NH");
  }

  public NHGraftonCountyCParser(String defCity, String defState) {
    super(NHGraftonCountyParser.CITY_LIST, defCity, defState);
  }

  @Override
  public String getFilter() {
    return "notification@nhpd.cloud";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strApt.equals("NH") && data.strCity.isEmpty()) {
      data.strState = data.strApt;
      data.strApt = "";
      String addr = data.strAddress;
      if (addr.endsWith(" NH") || addr.endsWith(" VT")) {
        data.strState = addr.substring(addr.length()-2);
        addr = addr.substring(0,addr.length()-3).trim();
      }
      addr = stripFieldEnd(addr, ",");
      data.strAddress = "";
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
    }
    return true;
  }

}
