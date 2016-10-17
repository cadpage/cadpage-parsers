package net.anei.cadpage.parsers.MA;

import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;

public class MANantucketCountyParser extends DispatchA63Parser {  
  public MANantucketCountyParser() {
    super("NANTUCKET COUNTY", "MA");
  }
  
  @Override
  public String getFilter() {
    return "CADNotify@nantucketpolice.com";
  }
}
