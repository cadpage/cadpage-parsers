package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

/**
 * Whitfield County, GA
 */

public class GAWhitfieldCountyCParser extends DispatchSPKParser {
  
  public GAWhitfieldCountyCParser() {
    super("WHITFIELD COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "wc911@whitfieldcountyga.com";
  }

}
