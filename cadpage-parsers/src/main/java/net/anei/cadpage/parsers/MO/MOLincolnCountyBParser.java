package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class MOLincolnCountyBParser extends DispatchH03Parser {

  public MOLincolnCountyBParser() {
    super("LINCOLN COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "LNCC@rejis.org";
  }
}
