package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class PAUnionCountyParser extends DispatchSPKParser {
  
  public PAUnionCountyParser() {
    super("UNION COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "cademail@unionco.org";
  }

}
