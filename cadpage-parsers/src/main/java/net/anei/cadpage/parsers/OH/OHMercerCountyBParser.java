package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OHMercerCountyBParser extends DispatchA19Parser {

  public OHMercerCountyBParser() {
    super("MERCER COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "noreply@mercercountysheriffohio.gov";
  }

  @Override
  public String adjustMapAddress(String addr) {
    return OHMercerCountyParser.adjustMapAddr(addr);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
