package net.anei.cadpage.parsers.MN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Dakota County, MN
 */
public class MNDakotaCountyParser extends DispatchA27Parser {
  
  public MNDakotaCountyParser() {
    super("DAKOTA COUNTY", "MN", "[A-Z]+\\d+[A-Z]?|[A-Z]{1,3}FD");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return COUNTY_NN_BLVD_PTN1.matcher(addr).replaceAll("$1_$2_$3");
  }
  private static final Pattern COUNTY_NN_BLVD_PTN1 = Pattern.compile("\\b(County) +(\\d+) +(BLVD)\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String postAdjustMapAddress(String addr) {
    return COUNTY_NN_BLVD_PTN2.matcher(addr).replaceAll("$1 $2 $3");
  }
  private static final Pattern COUNTY_NN_BLVD_PTN2 = Pattern.compile("\\b(County)_(\\d+)_(BLVD)\\b", Pattern.CASE_INSENSITIVE);
}
