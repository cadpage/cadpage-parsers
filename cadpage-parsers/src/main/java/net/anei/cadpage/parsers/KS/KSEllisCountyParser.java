package net.anei.cadpage.parsers.KS;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSEllisCountyParser extends DispatchA25Parser {
  
  public KSEllisCountyParser() {
    super("ELLIS COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "1-HPDDispatch@haysusa.com,cmccollum@haysusa.com";
  }
  
  private static final Pattern US_183_ALT_PTN = Pattern.compile("\\b183 +ALT(?: +HWY)?\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = US_183_ALT_PTN.matcher(addr).replaceAll("183 BYPASS");
    return addr;
  }
}
