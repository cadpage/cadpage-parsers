package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

public class OHMuskingumCountyEParser extends DispatchH06Parser {

  public OHMuskingumCountyEParser() {
    super("MUSKINGUM COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "dispatch@ohiomuskingumsheriff.org";
  }

}
