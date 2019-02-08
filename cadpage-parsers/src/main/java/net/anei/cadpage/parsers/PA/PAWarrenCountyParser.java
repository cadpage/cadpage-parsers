package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;

/**
 * Warren County, PA
 */
public class PAWarrenCountyParser extends DispatchCiscoParser {
  
  public PAWarrenCountyParser() {
    super("WARREN COUNTY", "PA");
    allowBadChars("()");
    setupSpecialStreets("PAGE HOLLOW");
  }
  
  @Override
  public String getFilter() {
    return "mnoe@warren-county.net,wc911text@warren-county.net";
  }
}
  