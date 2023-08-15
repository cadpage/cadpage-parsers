package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXHutchinsParser extends DispatchA18Parser {

  public TXHutchinsParser() {
    super(CITY_LIST, "HUTCHINS","TX");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  public String getFilter() {
    return "hpdcad@hutchinspd.org,crimes.helpdesk@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strApt.length() > 0) {
      if (data.strAddress.endsWith(" IH")) {
        data.strAddress = data.strAddress.substring(0, data.strAddress.length()-1) + ' ' + data.strApt;
        data.strApt = "";
      }
    }
    return true;
  }

  private static String[] CITY_LIST = new String[]{
      "DALLAS COUNTY",

      "HUTCHINS",
      "WILMER"
  };
}
