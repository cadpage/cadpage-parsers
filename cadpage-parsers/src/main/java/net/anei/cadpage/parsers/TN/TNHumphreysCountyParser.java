package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.general.GeneralParser;


public class TNHumphreysCountyParser extends GeneralParser {
  
  public TNHumphreysCountyParser() {
    super("HUMPHREYS COUNTY", "TN");
    setFieldList("CALL ADDR PLACE INFO");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911email.net";
  }

  @Override
  protected boolean isPageMsg(String subject, String body) {
    return true;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (! subject.equals("E911")) return false;
    return super.parseMsg("", body, data) ||
            data.parseGeneralAlert(this, body);
  }
}
