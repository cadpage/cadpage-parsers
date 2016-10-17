package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

/**
 * Oxford, OH

 */
public class OHOxfordParser extends DispatchSPKParser {
  
  public OHOxfordParser() {
    super("OXFORD", "OH");
  }
  
  @Override
  public String getFilter() {
    return "cisco@cityofoxford.org";
  }
}
