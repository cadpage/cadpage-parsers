package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Boone County, IL
 */

public class ILBooneCountyParser extends DispatchA27Parser {

  public ILBooneCountyParser() {
    super("BOONE COUNTY", "IL", "\\d{8}");
  }

  @Override
  public String getFilter() {
    return "noreply@cis.com,active911@boonecountyil.gov";
  }
}
