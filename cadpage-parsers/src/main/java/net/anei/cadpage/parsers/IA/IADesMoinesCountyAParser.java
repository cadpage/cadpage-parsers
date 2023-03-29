package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;


public class IADesMoinesCountyAParser extends DispatchA47Parser {

  public IADesMoinesCountyAParser() {
    super("DESCOM", CITY_LIST, "DES MOINES COUNTY", "IA", "[A-Z]{1,3}\\d{1,3}|\\d{1,3}|[A-Z]{1,3}RES");
  }

  @Override
  public String getFilter() {
    return "SWMAIL@BURLINGTONIOWA.ORG,swmail@descom.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    String extra = "";
    int pt = body.indexOf("\nReported:");
    if (pt >= 0) {
      extra = body.substring(0,pt).trim();
      body = body.substring(pt+1);
    }
    body = stripFieldStart(body, "TEST ONLY\n");
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCall = append(extra, " - ", data.strCall);
    return true;
  }

  private static final String[] CITY_LIST =new String[]{

      // Cities
      "BURLINGTON",
      "DANVILLE",
      "MEDIAPOLIS",
      "MIDDLETOWN",
      "WEST BURLINGTON",

      // Census-designated place
      "BEAVERDALE",

      // Other unincorporated communities
      "AUGUSTA",
      "DODGEVILLE",
      "KINGSTON",
      "SPERRY",
      "YARMOUTH",

      // Townships
      "BENTON TWP",
      "CONCORDIA TWP",
      "DANVILLE TWP",
      "FLINT RIVER TWP",
      "FRANKLIN TWP",
      "HURON TWP",
      "JACKSON TWP",
      "PLEASANT GROVE TWP",
      "TAMA TWP",
      "UNION TWP",
      "WASHINGTON TWP",
      "YELLOW SPRINGS TWP",

      // Henry County
      "NEW LONDON",

      // Louisa County
      "WAPELLO"
  };
}
