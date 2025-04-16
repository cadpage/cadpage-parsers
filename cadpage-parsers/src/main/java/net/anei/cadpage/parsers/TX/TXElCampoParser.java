
package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA53Parser;


public class TXElCampoParser extends DispatchA53Parser {

  public TXElCampoParser() {
    super("EL CAMPO", "TX");
  }

  @Override
  public String getFilter() {
    return "@cityofelcampo.org,@cityofelcampo1.onmicrosoft.com";
  }

  private static Pattern AVE_X_PTN = Pattern.compile("\\b(?:AVE|AVENUE) [A-Z]\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;

    // See if we can insert & in address
    // Unless there is an AVENUE X construct which will just confuse things
    if (!AVE_X_PTN.matcher(data.strAddress).find()) {
      String addr = data.strAddress;
      data.strAddress = "";
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_NO_IMPLIED_APT | FLAG_ANCHOR_END, addr, data);
    }
    return true;
  }
}
