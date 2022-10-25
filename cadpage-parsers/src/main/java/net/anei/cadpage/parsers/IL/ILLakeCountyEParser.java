package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class ILLakeCountyEParser extends DispatchH03Parser {

  public ILLakeCountyEParser() {
    super("LAKE COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "NWC@nwcdsalerts.org,NoReply@PremierOne_System";
  }
}
