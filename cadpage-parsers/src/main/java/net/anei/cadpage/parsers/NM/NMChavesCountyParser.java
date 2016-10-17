package net.anei.cadpage.parsers.NM;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NMChavesCountyParser extends DispatchOSSIParser {

  public NMChavesCountyParser() {
    super("CHAVES COUNTY", "NM",
        "FYI ADDR X/Z+? CALL! END");
  }

  @Override
  public String getFilter() {
    return "CAD@roswellpolice.com,Cad@roswell-nm.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("FYI:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
}
