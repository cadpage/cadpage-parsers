package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class MOStLouisCountyGParser extends DispatchH03Parser {

  public MOStLouisCountyGParser() {
    super("ST LOUIS COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "KRWC@rejis.org";
  }
}
