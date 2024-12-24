package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INLawrenceCountyParser extends DispatchA19Parser {

  public INLawrenceCountyParser() {
    super("LAWRENCE COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "spillmanrapid@bedford.in.gov";
  }

}
