package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.MsgInfo.Data;
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
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    if (!super.parseMsg(body, data)) return false;
    int pt = data.strCross.lastIndexOf("Add");
    if (pt >= 0) {
      if ("Additional Location Info:".startsWith(data.strCross.substring(pt))) {
        data.strCross = data.strCross.substring(0,pt).trim();
      }
    }
    return true;
  }
}
