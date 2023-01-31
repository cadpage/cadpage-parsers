package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;


/**
 * Lenawee County, MI (B)
 */
public class MILenaweeCountyBParser extends DispatchA9Parser {

  public MILenaweeCountyBParser() {
    super(null, "LENAWEE COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "IT@lenawee.mi.us";
  }

}
