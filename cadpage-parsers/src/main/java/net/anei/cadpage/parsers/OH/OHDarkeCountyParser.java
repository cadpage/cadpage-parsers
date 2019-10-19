package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class OHDarkeCountyParser extends DispatchA19Parser {

  public OHDarkeCountyParser() {
    super("DARKE COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "rroberts@darkecountysheriff.org,streborz@centurylink.net";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, "-VLG");
    data.strCity = stripFieldEnd(data.strCity, "-CITY");
    return true;
  }
  
}
