package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class MOCallawayCountyParser extends DispatchA25Parser {
 
  public MOCallawayCountyParser() {
    super("CALLAWAY COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "EnterpolAlerts@cceoc.org";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = BUS_54_PTN.matcher(addr).replaceAll("US 54 BUS");
    return addr;
  }
  private static final Pattern BUS_54_PTN = Pattern.compile("\\bBUS 54\\b", Pattern.CASE_INSENSITIVE);
}