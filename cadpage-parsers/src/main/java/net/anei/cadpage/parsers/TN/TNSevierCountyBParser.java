package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class TNSevierCountyBParser extends DispatchA71Parser {

  public TNSevierCountyBParser() {
    super("SEVIER COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "Central_Dispatch@mydomain.com,Central_Dispatch@seviercountytn.org";
  }

}
