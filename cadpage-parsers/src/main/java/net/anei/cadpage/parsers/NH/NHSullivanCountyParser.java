package net.anei.cadpage.parsers.NH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class NHSullivanCountyParser extends DispatchA32Parser {

  public NHSullivanCountyParser() {
    super(CITY_LIST, "SULLIVAN COUNTY", "NH");
  }

  @Override
  public String getFilter() {
    return "charlestowndispatch@gmail.com,lebanonpaging@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.endsWith(" Page")) subject = subject + " Page";
    return super.parseMsg(subject, body, data);
  }

  private static final String[] CITY_LIST = new String[] {
      // City
      "CLAREMONT",

      // Towns
      "ACWORTH",
      "CHARLESTOWN",
      "CORNISH",
      "CROYDON",
      "GOSHEN",
      "GRANTHAM",
      "LANGDON",
      "LEMPSTER",
      "NEWPORT",
      "PLAINFIELD",
      "SPRINGFIELD",
      "SUNAPEE",
      "UNITY",
      "WASHINGTON",

      // Census-designated places
      "CHARLESTOWN",
      "NEWPORT",
      "PLAINFIELD",

      // Other populated places
      "BALLOCH",
      "CORNISH FLAT",
      "EAST LEMPSTER",
      "GEORGES MILLS",
      "GUILD",
      "MERIDEN",
      "SOUTH ACWORTH"
  };

}
