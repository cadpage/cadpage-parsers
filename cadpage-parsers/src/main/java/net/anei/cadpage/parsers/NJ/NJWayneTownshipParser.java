package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;



public class NJWayneTownshipParser extends DispatchProphoenixParser {

  public NJWayneTownshipParser() {
    super(null, CITY_LIST, "WAYNE TOWNSHIP", "NJ");
  }

  @Override
  public String getFilter() {
    return "cad@waynetownship.com,5417047704,@mms.firstnet-mail.com,messaging@iamresponding.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Fix IAR edits
    if (subject.equals("Wayne")) {
      body = body.replace("\n{", "{");
    }
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.startsWith("PD")) data.strCity = "";
    return true;
  }

  private static final String[] CITY_LIST = new String[] {
      "WAYNE"
  };


}
