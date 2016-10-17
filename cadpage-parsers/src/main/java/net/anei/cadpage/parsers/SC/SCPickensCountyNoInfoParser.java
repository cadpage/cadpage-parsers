package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * This a special Active911 only variant parser that is identical to the real SCPickensCounty parser
 * except that it return no supplemental information
 */
public class SCPickensCountyNoInfoParser extends SCPickensCountyParser {

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strSupp = "";
    return true;
  }
}
