package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA75Parser;

public class OHPerryCountyParser extends DispatchA75Parser {

  public OHPerryCountyParser() {
    super("PERRY COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "PerryCountyDispatch@PerryCountyOhio.Net,JCPC911@perryco.org,alert@dispatch.perrycountyohio.gov";
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = TWP_RD_PTN.matcher(addr).replaceAll("TOWNSHIP HWY");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern TWP_RD_PTN = Pattern.compile("\\b(?:MONDAY CREEK|SALT LICK|[A-Z]+) TWP RD\\b");
}