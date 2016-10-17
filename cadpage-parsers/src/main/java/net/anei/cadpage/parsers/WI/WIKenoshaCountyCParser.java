package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;




public class WIKenoshaCountyCParser extends DispatchProphoenixParser {
  
  public WIKenoshaCountyCParser() {
    super("KENOSHA COUNTY", "WI");
  }
  
  @Override
  public String getFilter() {
    return "CAD@plprairiewi.com";
  }
}
