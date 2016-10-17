package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;

/**
 * Boone County, IN
 */
public class INBooneCountyAParser extends DispatchCiscoParser {

  public INBooneCountyAParser() {
    super("BOONE COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "ciscopaging@co.boone.in.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strAddress.startsWith("!")) data.strAddress = data.strAddress.substring(1).trim();
    return true;
  }
}
  