package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXAransasCountyParser extends DispatchA72Parser {

  public TXAransasCountyParser() {
    super("ARANSAS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "COR-Tyler-PSP@aransascounty.org";
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = BYPASS_35_PTN.matcher(addr).replaceAll("State 35$1 Business");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern BYPASS_35_PTN = Pattern.compile("\\b(?:Bypass|Bsn) 35( [NSEW])?\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("N Ocl") || city.equals("S Ocl")) return "Rockport";
    return super.adjustMapCity(city);
  }
}
