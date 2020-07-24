package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA38Parser;



public class SCDorchesterCountyAParser extends DispatchA38Parser {
  
  public SCDorchesterCountyAParser() {
    super("DORCHESTER COUNTY", "SC");
    setupMultiWordStreets("OLD ST GEORGE");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@dorchestercounty.net";
  }

}
