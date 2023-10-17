package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

/**
 * Colbert County, AL
 */
public class ALColbertCountyAParser extends DispatchA74Parser {

  public ALColbertCountyAParser() {
    super("COLBERT COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "CAD@colbert911.org";
  }
}
