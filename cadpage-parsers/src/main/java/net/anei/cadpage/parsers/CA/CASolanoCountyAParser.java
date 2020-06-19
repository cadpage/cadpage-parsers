package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

/**
 * Solano County, CA
 */
public class CASolanoCountyAParser extends DispatchA20Parser {
  
  public CASolanoCountyAParser() {
    super("SOLANO COUNTY", "CA", A20_UNIT_LABEL_REQ);
  }
  
  @Override
  public String getFilter() {
    return "irimssec@fairfield.ca.gov,@ci.vallejo.ca.us,@cityofvallejo.net,rimspaging@gmail.com,noreply-rims@fairfieldgov.onmicrosoft.com,@cityofvacaville.com";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = stripFieldEnd(addr, " OFF");
    addr = stripFieldEnd(addr, " ON");
    return super.adjustMapAddress(addr);
  }
}
