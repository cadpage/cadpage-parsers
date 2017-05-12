package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class WIKenoshaCountyDParser extends DispatchA57Parser {
  
  public WIKenoshaCountyDParser() {
    super("KENOSHA COUNTY", "WI");
  }
  
  WIKenoshaCountyDParser(String defState, String defCity) {
    super(defState, defCity);
  }
  
  @Override
  public String getFilter() {
    return "dispatch@kenoshajs.org";
  }
  
  @Override
  public String getAliasCode() {
    return "WIKenoshaCountyD";
  }

}
