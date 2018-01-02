package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class GAStephensCountyParser extends DispatchB2Parser {

  public GAStephensCountyParser() {
    super(CITY_LIST, "STEPHENS COUNTY", "GA", B2_FORCE_CALL_CODE);
    setupCallList((CodeSet)null);
  }
 
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(">\n", ">");
    return super.parseMsg(body, data);
  }

  private static final String[] CITY_LIST = new String[]{
      "AVALON",
      "EASTANOLLEE",
      "MARTIN",
      "TOCCOA"
  };
}
