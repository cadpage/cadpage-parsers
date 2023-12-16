package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class MDBaltimoreCountyDParser extends DispatchH03Parser {

  public MDBaltimoreCountyDParser() {
    super("BALTIMORE COUNTY", "MD");
  }

  @Override
  public String getFilter() {
    return "ECC@baltimorecountymd.gov";
  }
}
