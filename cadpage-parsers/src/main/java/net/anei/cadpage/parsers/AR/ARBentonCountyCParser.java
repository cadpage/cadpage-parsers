package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

/**
 *Bentonville, AR
 */
public class ARBentonCountyCParser extends DispatchH06Parser {

  public ARBentonCountyCParser() {
    super("BENTON COUNTY", "AR");
  }

  @Override
  public String getFilter() {
    return "noreply@bentonville.arkansas.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (!data.strApt.isEmpty()) {
      data.strAddress = stripFieldEnd(data.strAddress, ' '+data.strApt);
    }
    return true;
  }
}


