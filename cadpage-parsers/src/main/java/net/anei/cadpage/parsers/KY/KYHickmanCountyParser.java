package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class KYHickmanCountyParser extends DispatchA48Parser {

  public KYHickmanCountyParser() {
    super(CITY_LIST, "HICKMAN COUNTY", "KY", FieldType.PLACE, A48_OPT_CODE);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return super.parseMsg(subject, ": " + body, data);
  }

  private static final String[] CITY_LIST = new String[] {
      "CLINTON",
      "COLUMBUS",
      "HICKMAN"
  };

}
