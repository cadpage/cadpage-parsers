package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class OHGalliaCountyParser extends DispatchA19Parser {

  public OHGalliaCountyParser() {
    super("GALLIA COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "motorolaflex@galliacounty911.org";
  }
}
