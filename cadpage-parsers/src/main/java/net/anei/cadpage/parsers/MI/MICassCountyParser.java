package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class MICassCountyParser extends DispatchA1Parser {
  
  public MICassCountyParser() {
    super("CASS COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "Dispatcher@cassco.org,911_Copier@cassco.org,cpritchard@alertpss.com,dispatcher@porterfire.org,forwarding-noreply@google.com";
  }
}
