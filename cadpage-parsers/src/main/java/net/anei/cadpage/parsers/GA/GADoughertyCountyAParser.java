package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class GADoughertyCountyAParser extends DispatchOSSIParser {

  public GADoughertyCountyAParser() {
    super("DOUGHERTY COUNTY", "GA",
         "CALL ADDR X! X? INFO+");
  }

  @Override
  public String getFilter() {
    return "cad@dougherty.ga.us,777";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.equals("Text Message")) break;

      body = stripFieldStart(body, "City of Albany:");
      if (body.startsWith("(Text Message)")) {
        body = body.substring(14).trim();
        break;
      }

      return false;
    } while (false);

    int pt = body.indexOf("\n");
    if (pt >= 0) body = body.substring(0,pt).trim();

    if (!body.startsWith("CAD:")) body = "CAD:" + body;

    return super.parseMsg(body, data);
  }
}
