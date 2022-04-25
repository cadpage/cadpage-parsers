package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Bethel, CT
 */

public class CTBethelAParser extends DispatchA27Parser {
  
  public CTBethelAParser() {
    super("BETHEL", "CT", "[A-Z]+\\d+|[A-Z]+EMS|[A-Z]+FD|TWR");
  }
  
  @Override
  public String getFilter() {
    return "noreply@bethelpd.com";
  }
}
