package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class COMontezumaCountyBParser extends DispatchA64Parser {

  public  COMontezumaCountyBParser() {
    this("MONTEZUMA COUNTY");
  }

  COMontezumaCountyBParser(String defCity) {
    super(defCity, "CO");
  }

  @Override
  public String getFilter() {
    return "reports@messaging.eforcesoftware.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.contains("City State County:")) return false;
    return super.parseMsg(subject, body, data);
  }


}
