package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA88Parser;

public class NCScotlandCountyParser extends DispatchA88Parser {

  public NCScotlandCountyParser() {
    super(CITY_LIST, "SCOTLAND COUNTY", "NC");
  }

  @Override
  public String getFilter() {
    return "scotlandcountync911@gmail.com,dispatch@scotlandcounty.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.startsWith("-- ")) body = "";
    return super.parseMsg(subject, body, data);
  }

  private static final String[] CITY_LIST = new String[] {
      // City
      "LAURINBURG",

      // Towns
      "GIBSON",
      "MAXTON",
      "WAGRAM",

      // Census-designated places
      "DEERCROFT",
      "LAUREL HILL",
      "OLD HUNDRED",
      "SCOTCH MEADOWS",

      // Other unincorporated communities
      "EAST LAURINBURG",
      "MONTCLAIR",

      // Townships
      "LAUREL HILL",
      "SPRING HILL",
      "STEWARTSVILLE",
      "WILLIAMSON"
  };

}
