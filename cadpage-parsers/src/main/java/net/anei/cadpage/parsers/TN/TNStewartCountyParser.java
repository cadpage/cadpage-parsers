package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA89Parser;

public class TNStewartCountyParser extends DispatchA89Parser {

  public TNStewartCountyParser() {
    super("STEWART COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "StewartTNDispatch@911comm2.info";
  }

}
