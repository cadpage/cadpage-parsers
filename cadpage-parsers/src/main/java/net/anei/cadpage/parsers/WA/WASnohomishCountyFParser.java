package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class WASnohomishCountyFParser extends DispatchA57Parser {
  
  public WASnohomishCountyFParser() {
    super("SNOHOMISH COUNTY", "WA");
  }
  
  @Override
  public String getFilter() {
    return "NoReply@snopac911.us";
  }
}
