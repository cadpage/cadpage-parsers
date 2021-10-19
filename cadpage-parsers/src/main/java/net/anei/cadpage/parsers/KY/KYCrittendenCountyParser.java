package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class KYCrittendenCountyParser extends SmartAddressParser {

  public KYCrittendenCountyParser() {
    super("CRITTENDEN COUNTY", "KY");
    setFieldList("CALL PLACE ADDR APT");
  }

  @Override
  public String getFilter() {
    return "page@relay2.thefirehorn.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Page|TheFireHorn/NextDispatch")) return false;
    if (body.startsWith("-")) body = ' ' + body;
    int pt = body.indexOf(" - ");
    if (pt < 0) return false;

    data.strCall = body.substring(0,pt).trim();
    String addr = body.substring(pt+3).trim();

    parseAddress(StartType.START_PLACE, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
    if (data.strAddress.isEmpty()) {
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }
    data.strApt = stripFieldStart(data.strApt, "-");
    return true;
  }

}
