package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXBurnetCountyAParser extends DispatchA72Parser {
  
  public TXBurnetCountyAParser() {
    this("BURNET COUNTY", "TX");
  }
  
  public TXBurnetCountyAParser(String defCity, String defState) {
    super(defCity, defState);
  }
  
  @Override
  public String getFilter() {
    return "alert@burnetsheriff.com";
  }
  
  private static final Pattern RR_PTN = Pattern.compile("\\bRR\\b");
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = RR_PTN.matcher(addr).replaceAll("RANCH ROAD");
    return super.adjustMapAddress(addr);
  }
}
