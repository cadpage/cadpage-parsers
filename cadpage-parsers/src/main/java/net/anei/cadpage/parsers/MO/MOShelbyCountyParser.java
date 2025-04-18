package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA62Parser;

public class MOShelbyCountyParser extends DispatchA62Parser {

  public MOShelbyCountyParser() {
    super("SHELBY COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "sc911alerts@gmail.com,@idsapplications.com";
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = SHELBYNNN_PTN.matcher(addr).replaceAll("COUNTY ROAD $1");
    addr = RT_PTN.matcher(addr).replaceAll("HWY");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern SHELBYNNN_PTN = Pattern.compile("\\bSHELBY +(\\d+)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern RT_PTN = Pattern.compile("\\bRT\\b", Pattern.CASE_INSENSITIVE);
}
