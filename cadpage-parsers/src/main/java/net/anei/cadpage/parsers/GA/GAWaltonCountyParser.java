package net.anei.cadpage.parsers.GA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Walton County, GA
 */
public class GAWaltonCountyParser extends DispatchA19Parser {
  
  public GAWaltonCountyParser() {
    super("WALTON COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "@spillman.co.walton.ga.us,@co.walton.ga.us";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = RDG_PTN.matcher(addr).replaceAll("RIDGE");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern RDG_PTN = Pattern.compile("\\bRDG\\b", Pattern.CASE_INSENSITIVE);
}
