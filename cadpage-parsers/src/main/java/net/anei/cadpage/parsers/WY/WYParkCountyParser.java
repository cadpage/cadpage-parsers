package net.anei.cadpage.parsers.WY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

/**
 * Park County, WY
 */
public class WYParkCountyParser extends DispatchA20Parser {

  public WYParkCountyParser() {
    super("PARK COUNTY", "WY");
  }

  @Override
  public String getFilter() {
    return "@parkcountysheriff-wy.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("(")) subject = "Dispatched Call " + subject;
    return super.parseMsg(subject, body, data);
  }

}
