package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

/**
 *Bentonville, AR
 */
public class ARBentonCountyCParser extends DispatchA9Parser {
  
  public ARBentonCountyCParser() {
    super("BENTON COUNTY", "AR");
  }
  
  @Override
  public String getFilter() {
    return "BentonvillePD@bentonville.arkansas.gov";
  }
}


