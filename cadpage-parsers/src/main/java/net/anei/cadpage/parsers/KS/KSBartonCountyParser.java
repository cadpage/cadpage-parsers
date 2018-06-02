package net.anei.cadpage.parsers.KS;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSBartonCountyParser extends DispatchA25Parser {


  public KSBartonCountyParser() {
    super("BARTON COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "bcso@ruraltel.net,alerts@bartoncounty.org";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = K_NN_HWY_PTN.matcher(addr).replaceAll("KS $1");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern K_NN_HWY_PTN = Pattern.compile("\\bK (\\d+) HWY\\b", Pattern.CASE_INSENSITIVE);
}
