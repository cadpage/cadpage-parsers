package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class KYBooneCountyCParser extends DispatchEmergitechParser {

  public KYBooneCountyCParser() {
    super(CITY_LIST, "BOONE COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "@cvgairport.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    subject = stripFieldStart(subject, "Text Message|");
    return super.parseMsg(subject, body, data);
  }

  private static final String[] CITY_LIST  = new String[] {

      // Cities
      "FLORENCE",
      "UNION",
      "WALTON",

      // Census-designated places
      "BURLINGTON",
      "FRANCISVILLE",
      "HEBRON",
      "OAKBROOK",
      "PETERSBURG",
      "RABBIT HASH",
      "VERONA",

      // Other unincorporated communities
      "BELLEVIEW",
      "BIG BONE",
      "BULLITTSVILLE",
      "CONSTANCE",
      "HAMILTON",
      "LIMABURG",
      "MCVILLE",
      "RICHWOOD",
      "TAYLORSPORT"
  };

}
