package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MODadeCountyParser extends DispatchA48Parser {

  public MODadeCountyParser() {
    super(CITY_LIST, "DADE COUNTY", "MO", FieldType.INFO, A48_NO_CODE);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    subject = "As of 99/99/99 99:99:99";
    body = body.replace(" CITY OF ", " ");
    if (!super.parseMsg(subject, body, data)) return false;
    data.strDate = data.strTime = "";
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities and towns
      "ARCOLA",
      "DADEVILLE",
      "EVERTON",
      "GREENFIELD",
      "LOCKWOOD",
      "SOUTH GREENFIELD",

      // Unincorporated communities
      "BONA",
      "CEDARVILLE",
      "COMET",
      "CORRY",
      "DUDENVILLE",
      "KINGS POINT",
      "MEINERT",
      "PENNSBORO",
      "SYLVANIA"
  };
}
