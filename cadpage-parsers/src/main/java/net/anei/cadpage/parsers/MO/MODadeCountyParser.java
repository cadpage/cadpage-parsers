package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MODadeCountyParser extends DispatchA48Parser {

  public MODadeCountyParser() {
    super(CITY_LIST, "DADE COUNTY", "MO", FieldType.INFO, A48_NO_CODE);
    setupCallList(CALL_LIST);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    subject = "As of 99/99/99 99:99:99";
    body = body.replace(" CITY OF ", " ");
    if (!super.parseMsg(subject, body, data)) return false;
    data.strDate = data.strTime = "";
    return true;
  }

  private static final String[] CALL_LIST = new String[] {
      "911HUP",
      "ASSIST OTHER AGENCY",
      "C&I",
      "FIRES",
      "MEDICAL ALARM",
      "MEDICAL EVENT",
      "MVA",
      "OUTSIDE FIRE",
      "STRUCTURE FIRE",
      "SUSPAC",
      "TEST CALL",
      "TEST CALL ;DCES TEST CAD FOR EMAIL DELIVERY",
      "UNK",
      "VIOLAT",
  };

  private static final String[] CITY_LIST = new String[] {

      // Cities and towns
      "ARCOLA",
      "DADEVILLE",
      "EVERTON",
      "GREENFIELD",
      "CITY OF GREENFIELD",
      "LOCKWOOD",
      "CITY OF LOCKWOOD",
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
      "SYLVANIA",

      // Barton County
      "GOLDEN CITY",
      "LAMAR",

      // Polk County
      "ALDRICH",

      // Greene County
      "ASH GROVE",
      "WALNUT GROVE",

      // Jasper County
      "JASPER",

      // Cedar County
      "JERICO SPRINGS",
      "STOCKTON",

      // Lawrence County
      "MILLER"
  };
}
