package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class LAJeffersonDavisParishParser extends DispatchA55Parser {

  public LAJeffersonDavisParishParser() {
    this("JEFFERSON DAVIS PARISH", "LA");
  }

  LAJeffersonDavisParishParser(String defCity, String defState) {
    super(defCity, defState);
  }

  public String getAliasCode() {
    return "LAJeffersonDavisParish";
  }

  public String getFilter() {
    return "cadalerts@messaging.eforcesoftware.net,reports@messaging.eforcesoftware.net";
  }
}
