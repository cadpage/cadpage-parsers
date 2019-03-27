package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;



public class KYAndersonCountyParser extends DispatchSPKParser {
  
  public KYAndersonCountyParser() {
    super("ANDERSON COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "noreply@interact911.com,noreply@public-safety-cloud.com";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return BYPASS_PTN.matcher(addr).replaceAll("US 127");
  }
  private static final Pattern BYPASS_PTN = Pattern.compile("\\bBYPASS\\b", Pattern.CASE_INSENSITIVE);

}
