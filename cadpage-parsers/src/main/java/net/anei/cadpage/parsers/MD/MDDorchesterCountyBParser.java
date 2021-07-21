package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchX01Parser;

public class MDDorchesterCountyBParser extends DispatchX01Parser {

  public MDDorchesterCountyBParser() {
    super(MDDorchesterCountyParser.CITY_CODES, "DORCHESTER COUNTY", "MD");
    setupMapAdjustReplacements(MDDorchesterCountyParser.MAP_ADJ_TABLE);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!body.startsWith("Dorchester: ")) return false;
    body = body.substring(12).trim();
    body = stripFieldStart(body, "fromcad CAD ");
    return super.parseHtmlMsg(subject, body, data);
  }
}
