package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class MDPrinceGeorgesCountyIParser extends DispatchH03Parser {

  public MDPrinceGeorgesCountyIParser() {
    super("PRINCE GEORGES COUNTY", "MD");
  }

  @Override
  public String getFilter() {
    return "PSC@co.pg.md.us";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;

    data.strCall = convertCodes(data.strCall, MDPrinceGeorgesCountyGParser.CALL_CODES);

    if (data.strCity.equals("OUT")) {
      if (!data.strApt.isEmpty()) {
        data.strCity = data.strApt;
        data.strApt = "";
      } else {
        data.strCity = "OOC";
      }
    }
    return true;
  }
}
