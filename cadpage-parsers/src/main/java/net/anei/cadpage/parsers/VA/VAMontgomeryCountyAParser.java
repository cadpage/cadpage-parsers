package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * Montgomery County, VA
 */
public class VAMontgomeryCountyAParser extends DispatchSouthernParser {
    
  public VAMontgomeryCountyAParser() {
    super(CITY_LIST, "MONTGOMERY COUNTY", "VA", DSFLAG_LEAD_PLACE | DSFLAG_FOLLOW_CROSS);
    setupMultiWordStreets(
        "COAL BANK HOLLOW",
        "GREEN MEADOW",
        "HUNT CLUB",
        "INDUSTRIAL PARK",
        "PATRICK HENRY",
        "PRICES FORK",
        "TALL OAKS",
        "TOMS CREEK",
        "UNIVERSITY CITY",
        "WEST CAMPUS"
    );
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("(BYPASS)"," BYPASS ").trim();
    return super.parseMsg(body, data);
  }

  private static final String[] CITY_LIST = new String[]{
    "ALLEGHANY SPRINGS",
    "BLACKSBURG",
    "BRADSHAW",
    "CHILDRESS",
    "CHRISTIANSBURG",
    "ELLETT",
    "ELLISTON-LAFAYETTE",
    "GRAYSONTOWN",
    "IRONTO",
    "LONG SHOP",
    "LUSTERS GATE",
    "MERRIMAC",
    "MCCOY",
    "MCDONALDS MILL",
    "PILOT",
    "PRICES FORK",
    "RINER",
    "ROGERS",
    "SHAWSVILLE",
    "SUGAR GROVE",
    "VICKER",
    "WALTON",
    "TOM'S CREEK",
    "YELLOW SULPHUR SPRINGS",
    "PLUM CREEK"
  };
}